package systemtests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.see.client.network.DefaultClient;
import com.see.client.network.TradeListener;
import com.see.common.domain.OrderType;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.NoLoginException;
import com.see.common.message.OrderMessage;
import com.see.common.message.TradeResponse;
import com.see.server.StockExchange;
import com.see.server.business.ServiceContainer;

public class SystemTests extends TestCase {

	private class ClientFake {
		private TradeListener responseObserver = new TradeListener() {
			@Override
			public void onTrade(TradeResponse response) {
				responses.add(response);
			}
		};
		public DefaultClient client = new DefaultClient();
		public ArrayList<TradeResponse> responses = new ArrayList<>();
		public ArrayList<OrderMessage> orders = new ArrayList<>();

		public ClientFake() {
			client.addObserver(responseObserver);
		}

		public UUID sendOrder(OrderMessage order) throws BadOrderException {
			orders.add(order);
			return client.sendOrder(order);
		}

		public void sendCancel(UUID orderID) throws BadOrderException,
				CancelOrderException {
			client.cancelOrder(orderID);
		}

	}

	static StockExchange stockExchange;
	static ClientFake client1;
	static ClientFake client2;

	@Before
	public static void init() {
	}

	@Test
	public void test_SimpleCancelAction() throws NoLoginException,
			BadOrderException, IOException {
		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final String[] tickerSymbols = { "AAPL", "MCD", "IBM",
							"MSFT", "PG" };
					ServiceContainer container = new ServiceContainer(
							tickerSymbols);
					stockExchange = new StockExchange(container);
					stockExchange.runServer();
				} catch (IOException e) {
				}

			}
		});
		serverThread.start();
		Thread clint1Thread = new Thread(new Runnable() {

			@Override
			public void run() {

				client1 = new ClientFake();
				try {
					client1.client.login("Alice");
					OrderMessage order = new OrderMessage("Alice", "IBM",
							(float) 1.0, 1, OrderType.BUY);
					UUID orderUuid = client1.sendOrder(order);
					client1.sendCancel(orderUuid);
				} catch (NoLoginException | BadOrderException
						| CancelOrderException e) {
					fail();
				}
			}

		});
		clint1Thread.start();

	}

	@Test
	public void test_SimpleTradeAction() throws NoLoginException,
			BadOrderException, IOException {
		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					final String[] tickerSymbols = { "AAPL", "MCD", "IBM",
							"MSFT", "PG" };
					ServiceContainer container = new ServiceContainer(
							tickerSymbols);
					stockExchange = new StockExchange(container);
					stockExchange.runServer();
				} catch (IOException e) {
				}

			}
		});
		serverThread.start();
		Thread clint1Thread = new Thread(new Runnable() {

			@Override
			public void run() {

				client1 = new ClientFake();
				try {
					client1.client.login("Alice");
					client1.sendOrder(new OrderMessage("Alice", "IBM",
							(float) 1.0, 1, OrderType.BUY));
					assertEquals(client1.responses.size(), 1);
				} catch (NoLoginException | BadOrderException e) {
					fail();
				}
			}

		});
		Thread clint2Thread = new Thread(new Runnable() {

			@Override
			public void run() {
				client2 = new ClientFake();
				try {
					client2.client.login("Bob");
					client2.sendOrder(new OrderMessage("Bob", "IBM",
							(float) 1.0, 1, OrderType.SELL));
					assertEquals(client2.responses.size(), 0);
					assertEquals(client2.responses.get(0).getQuantity(), 1);
					assertEquals(client2.responses.get(0).getCounterpart(),
							"client1");
				} catch (NoLoginException | BadOrderException e) {
					fail();
				}

			}

		});
		clint1Thread.start();
		clint2Thread.start();

	}
}
