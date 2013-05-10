package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.domain.CancelOrder;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderVerifier;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.TradeOrder;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.business.ServiceContainer;

public class StockExchange {
	private static Logger log = Logger.getLogger(StockExchange.class.getName());
	private ServerSocket serverSocket;
	private final static int DEFAULT_PORT = 2006;
	private static final int DEFAULT_QUEUE_LENGTH = 100;
	private ServiceContainer serviceContainer;
	private HashMap<String, ObjectOutputStream> clientOutStreamMap = new HashMap<>();
	private int orderCount = 0;

	private synchronized int generateOrderID() {
		return orderCount++;
	}

	public StockExchange(ServiceContainer container) {
		this.serviceContainer = container;
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
		final ObjectInputStream in;
		try {
			in = new ObjectInputStream(newClientSocket.getInputStream());
			out = new ObjectOutputStream(newClientSocket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			log.warning("Unable to create connection: " + e.getMessage());
			try {
				newClientSocket.close();
			} catch (IOException e1) {
				log.warning("Unable to close connection: " + e1.getMessage());
			}
			return;
		}
		String message = null;
		try {
			message = (String) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			log.warning("Unable to parse client login: " + e.getMessage()
					+ " .Connection closed");
			try {
				in.close();
				out.close();
				newClientSocket.close();
				return;
			} catch (IOException e1) {
				log.warning("Unable to close connection: " + e1.getMessage());
			}
		}

		final String login = message;
		if (clientOutStreamMap.containsKey(login)) {
			sendMessage(out, String.format(
					"User with login=%s already connected", login));
			try {
				in.close();
				out.close();
				newClientSocket.close();
				return;
			} catch (IOException e1) {
				log.warning("Unable to close connection: " + e1.getMessage());
			}
		} else {
			LinkedList<Response> delayedResponses = serviceContainer
					.getDelayedResponses(login);
			if (delayedResponses != null) {
				for (Response response : delayedResponses)
					sendMessage(out, response);
				log.info(String.format(
						"Sending %d delayed responses to client=%s",
						delayedResponses.size(), login));
			} else {
				log.info(String.format("No delayed responses for client=%s",
						login));
			}

			sendMessage(out, "Ok");

		}
		clientOutStreamMap.put(login, out);

		log.info(String.format("New client connected: login=%s", login));
		FilledObserver observer = new FilledObserver(message) {
			@Override
			public void onFilled(Response response) {
				if (clientOutStreamMap.containsKey(login)) {
					log.info(String
							.format("Send response to client: client=%s about order: orderID=%d",
									login, response.getOrderID()));
					sendMessage(out, response);
				} else {
					log.info(String
							.format("Add delayed response to client: client=%s about order: orderID=%d",
									login, response.getOrderID()));
					serviceContainer.addDelayedResponse(response);
				}
			}
		};

		serviceContainer.addObserver(observer);
		listenClient(login, in, out, newClientSocket);
	}

	private void listenClient(final String login, final ObjectInputStream in,
			final ObjectOutputStream out, final Socket clientSocket) {
		new Thread() {
			public void run() {
				while (true) {
					try {
						Object message = in.readObject();
						Order order;
						OrderVerifier orderVerifier = new OrderVerifier();
						if (message instanceof String) {
							if ("disconnect".equals((String) message)) {
								disconnectClient(login, clientSocket, in, out);
								return;
							}
							continue;
						} else {
							if (message instanceof Order) {
								order = (Order) message;
							} else
								continue;
						}
						if (order instanceof CancelOrder) {
							order.setDate(new Date());
							sendMessage(out, order.getCancelingOrderID());

						} else {
							int newOrderID = generateOrderID();
							try {
								orderVerifier.verifyTradeOrder(
										(TradeOrder) order,
										serviceContainer.getTickerSymbols());
							} catch (BadOrderException e) {
								sendMessage(clientOutStreamMap.get(login), -1);
								continue;
							}
							order.setOrderID(newOrderID);
							order.setDate(new Date());
							sendMessage(clientOutStreamMap.get(login),
									newOrderID);
						}
						serviceContainer.sendOrder(order);
						log.info(String
								.format("Add new order: orderID=%d from client: client=%s",
										order.getCancelingOrderID(), login));
					} catch (ClassNotFoundException e) {
						log.info(String.format("Bad clien request: client=%s",
								login));
					} catch (SocketException closeException) {
						disconnectClient(login, clientSocket, in, out);
						return;

					} catch (IOException e) {
					}

				}
			}

		}.start();
	}

	private void disconnectClient(String login, Socket socket,
			ObjectInputStream in, ObjectOutputStream out) {
		try {
			in.close();
			out.close();
			socket.close();
			clientOutStreamMap.remove(login);
			log.info(String.format("Client disconnected: client=%s", login));
		} catch (IOException e) {
			log.warning(String.format("Unable to disconnect: client=%s", login));
		}
	}

	void sendMessage(ObjectOutputStream out, final Object response) {
		try {
			out.writeObject(response);
			out.flush();
		} catch (IOException ioException) {
			log.warning("Unable to send message");
		}
	}

	public static void main(String[] args) throws IOException {
		final String[] tickerSymbols = { "AAPL", "MCD", "IBM", "MSFT", "PG" };
		ServiceContainer container = new ServiceContainer(tickerSymbols);
		StockExchange stockExchange = new StockExchange(container);
		stockExchange.runServer();
	}
}
