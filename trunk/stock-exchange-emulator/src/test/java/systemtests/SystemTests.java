package systemtests;
import java.io.IOException;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.see.client.network.Client;
import com.see.client.network.ResponseObserver;
import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;
import com.see.common.domain.Status;
import com.see.common.domain.TradeOperation;
import com.see.common.domain.TradeOrder;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.NoLoginException;
import com.see.server.StockExchange;
import com.see.server.business.ServiceContainer;

public class SystemTests extends TestCase {

	private class ClientFake {
		private ResponseObserver responseObserver = new ResponseObserver() {
			@Override
			public void onResponse(ClientResponse response) {
				responses.add(response);
			}
		};
		public Client client = new Client();
		public ArrayList<ClientResponse> responses = new ArrayList<>();
		public ArrayList<Order> orders = new ArrayList<>();

		public ClientFake() {
			client.addObserver(responseObserver);
		}

		public void sendOrder(Order order) throws BadOrderException {
			orders.add(order);
			client.sendOrder(order);
		}

	}

	static StockExchange stockExchange;
	static ClientFake client1;
	static ClientFake client2;

	@Before
	public static void init() {
		final String[] tickerSymbols = { "AAPL", "MCD", "IBM", "MSFT", "PG" };
		ServiceContainer container = new ServiceContainer(tickerSymbols);
		stockExchange = new StockExchange(container);
	}

	@Test
	public void test_SimpleTradeAction() throws NoLoginException,
			BadOrderException, IOException {
		Thread serverThread = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
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
					client1.sendOrder(new TradeOrder("Alice", "IBM",
							TradeOperation.BID, 1, (float) 1.0));
					assertEquals(client1.responses.size(), 1);
				} catch (NoLoginException | BadOrderException e) {
				}
			}

		});
		Thread clint2Thread = new Thread(new Runnable() {

			@Override
			public void run() {
				client2 = new ClientFake();
				try {
					client2.client.login("Bob");
					client2.sendOrder(new TradeOrder("Bob", "IBM",
							TradeOperation.OFFER, 1, (float) 1.0));
					assertEquals(client2.responses.size(), 1);
					assertEquals(client2.responses.get(0).getStatus(),
							Status.FULLY_FILLED);
				} catch (NoLoginException | BadOrderException e) {
				}

			}

		});
		clint1Thread.start();
		clint2Thread.start();

	}
}
