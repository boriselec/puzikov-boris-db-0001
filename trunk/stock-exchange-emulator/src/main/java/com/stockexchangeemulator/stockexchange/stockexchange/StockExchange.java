package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.WrappedOrder;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.business.OrderBookService;

public class StockExchange {
	private ServerSocket serverSocket;
	private final static int DEFAULT_PORT = 2006;
	private static final int DEFAULT_QUEUE_LENGTH = 10;
	// TODO: define ticker symbols in file
	private final static String[] TICKER_SYMBOLS = { "AAPL", "MCD", "IBM",
			"MSFT", "PG" };
	private HashMap<String, OrderBookService> serviceContainer;
	private HashMap<Integer, ObjectOutputStream> clientOutStreamMap = new HashMap<>();
	private int clientCount = 0;
	private volatile int orderCount = 0;

	private HashMap<String, OrderBookService> createServiceContainer(
			String[] tickers) {
		HashMap<String, OrderBookService> result = new HashMap();

		for (String ticker : tickers) {
			result.put(ticker, new OrderBookService());
		}
		return result;
	}

	public StockExchange() {
		serviceContainer = createServiceContainer(TICKER_SYMBOLS);
		for (String ticker : TICKER_SYMBOLS) {
			OrderBookService orderBookService = serviceContainer.get(ticker);
		}
	}

	public static void main(String[] args) throws IOException {
		StockExchange stockExchange = new StockExchange();
		stockExchange.runServer();
	}

	private void runServer() throws IOException {
		serverSocket = new ServerSocket(DEFAULT_PORT, DEFAULT_QUEUE_LENGTH);
		while (true) {
			Socket newClient = serverSocket.accept();
			System.out.println("new client");
			createNewClient(newClient);
		}
	}

	private void createNewClient(Socket newClientSocket) {
		final ObjectOutputStream out;
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(newClientSocket.getInputStream());
			out = new ObjectOutputStream(newClientSocket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		int clientID = generateClientID();
		clientOutStreamMap.put(clientID, out);

		FilledObserver observer = new FilledObserver(clientID) {
			@Override
			public void onFilled(Response response) {
				sendMessage(out, response);
			}
		};

		addObserverToAll(observer);

		sendMessage(out, clientID);
		listenClient(clientID, in);
	}

	private void listenClient(final Integer clientID, final ObjectInputStream in) {
		new Thread() {
			public void run() {
				while (true) {
					try {
						Order order = (Order) in.readObject();
						WrappedOrder wrappedOrder;
						if (order.getType() == Operation.CANCEL) {
							sendMessage(clientOutStreamMap.get(clientID),
									order.getOrderID());
							wrappedOrder = new WrappedOrder(clientID,
									order.getOrderID(), order, new Date());

						} else {
							int newOrderID = generateOrderID();
							sendMessage(clientOutStreamMap.get(clientID),
									newOrderID);
							wrappedOrder = new WrappedOrder(clientID,
									newOrderID, order, new Date());
						}
						serviceContainer.get(order.getStockName()).sendOrder(
								wrappedOrder);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		}.start();
	}

	private int generateOrderID() {
		// TODO: check multithread problems
		return orderCount++;
	}

	private void addObserverToAll(FilledObserver observer) {
		for (String ticker : TICKER_SYMBOLS)
			serviceContainer.get(ticker).addObserver(observer);
	}

	private int generateClientID() {
		// TODO: generate true unique
		return clientCount++;
	}

	void sendMessage(ObjectOutputStream out, final Object response) {
		try {
			out.writeObject(response);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
