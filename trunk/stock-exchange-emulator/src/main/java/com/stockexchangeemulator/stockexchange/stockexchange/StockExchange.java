package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderVerifier;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.WrappedOrder;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.business.OrderBookService;

public class StockExchange {
	private static Logger log = Logger.getLogger(StockExchange.class.getName());
	private ServerSocket serverSocket;
	private final static int DEFAULT_PORT = 2006;
	private static final int DEFAULT_QUEUE_LENGTH = 100;
	private final static String[] TICKER_SYMBOLS = { "AAPL", "MCD", "IBM",
			"MSFT", "PG" };
	private HashMap<String, OrderBookService> serviceContainer;
	private HashMap<Integer, ObjectOutputStream> clientOutStreamMap = new HashMap<>();
	private LinkedBlockingQueue<Response> delayedResponses = new LinkedBlockingQueue<>();
	private int clientCount = 0;
	private int orderCount = 0;

	private HashMap<String, OrderBookService> createServiceContainer(
			String[] tickers) {
		HashMap<String, OrderBookService> result = new HashMap<String, OrderBookService>();

		for (String ticker : tickers) {
			result.put(ticker, new OrderBookService());
		}
		return result;
	}

	public StockExchange() {
		serviceContainer = createServiceContainer(TICKER_SYMBOLS);
	}

	public static void main(String[] args) throws IOException {
		StockExchange stockExchange = new StockExchange();
		stockExchange.runServer();
	}

	private void runServer() throws IOException {
		serverSocket = new ServerSocket(DEFAULT_PORT, DEFAULT_QUEUE_LENGTH);
		log.info("Stock exchange started");
		while (true) {
			Socket newClient = serverSocket.accept();
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

		final int clientID = generateClientID();
		clientOutStreamMap.put(clientID, out);

		log.info(String.format("New client connected: clientID=%d", clientID));
		FilledObserver observer = new FilledObserver(clientID) {
			@Override
			public void onFilled(Response response) {
				if (clientOutStreamMap.containsKey(clientID)) {
					log.info(String
							.format("Send response to client: clientID=%d about order: orderID=%d",
									clientID, response.getOrderID()));
					sendMessage(out, response);
				} else {
					log.info(String
							.format("Add delayed response to client: clientID=%d about order: orderID=%d",
									clientID, response.getOrderID()));
					delayedResponses.add(response);
				}
			}
		};

		addObserverToAll(observer);

		sendMessage(out, clientID);
		listenClient(clientID, in, out, newClientSocket);
	}

	private void listenClient(final Integer clientID,
			final ObjectInputStream in, ObjectOutputStream out,
			final Socket clientSocket) {
		new Thread() {
			public void run() {
				while (true) {
					try {
						Object message = in.readObject();
						Order order;
						OrderVerifier orderVerifier = new OrderVerifier();
						if (message instanceof String) {
							if ("disconnect".equals((String) message)) {
								disconnectClient(clientID, clientSocket);
								return;
							}
							continue;
						} else {
							if (message instanceof Order)
								order = (Order) message;
							else
								continue;
						}
						WrappedOrder wrappedOrder;
						if (order.getType() == Operation.CANCEL) {
							sendMessage(clientOutStreamMap.get(clientID),
									order.getOrderID());
							wrappedOrder = new WrappedOrder(clientID,
									order.getOrderID(), order, new Date());

						} else {
							int newOrderID = generateOrderID();
							try {
								orderVerifier
										.verifyOrder(order, TICKER_SYMBOLS);
							} catch (BadOrderException e) {
								sendMessage(clientOutStreamMap.get(clientID),
										-1);
								continue;
							}
							sendMessage(clientOutStreamMap.get(clientID),
									newOrderID);
							wrappedOrder = new WrappedOrder(clientID,
									newOrderID, order, new Date());
						}
						serviceContainer.get(order.getStockName()).sendOrder(
								wrappedOrder);
						log.info(String
								.format("Add new order: orderID=%d from client: clientID=%d",
										order.getOrderID(), clientID));
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SocketException closeException) {
						disconnectClient(clientID, clientSocket);
						return;

					} catch (IOException e) {
					}

				}
			}

		}.start();
	}

	private void disconnectClient(int clientID, Socket socket) {
		try {
			socket.close();
			clientOutStreamMap.remove(clientID);
			log.info(String
					.format("Client disconnected: clientID=%d", clientID));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized int generateOrderID() {
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
