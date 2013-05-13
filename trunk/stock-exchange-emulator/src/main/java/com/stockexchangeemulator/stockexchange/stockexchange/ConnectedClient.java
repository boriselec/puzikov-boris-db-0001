package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.OrderVerifier;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.business.ServiceContainer;

public class ConnectedClient implements Runnable {
	private static Logger log = Logger.getLogger(ConnectedClient.class
			.getName());

	private String login;
	private HashSet<String> clientMap;
	private ServiceContainer serviceContainer;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	private Messager messager;

	private int orderCount = 0;

	private int generateOrderID() {
		return orderCount++;
	}

	public ConnectedClient(ServiceContainer serviceContainer,
			HashSet<String> clientMap, Socket socket) {
		this.serviceContainer = serviceContainer;
		this.clientMap = clientMap;
		this.socket = socket;
		createConnection(socket);
		this.messager = new Messager(in, out);
	}

	@Override
	public void run() {

		try {
			checkConnection();
			sendDelayedResponses(out);

			clientMap.add(login);
			createObserver();

			log.info(String.format("New client connected: login=%s", login));
			listenClient();
		} catch (IOException e) {
			messager.sendError("Server IO error: " + e.getMessage());
		}

	}

	private void createConnection(Socket socket) {
		try {
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
		} catch (IOException e) {
			log.warning("Unable to create connection: " + e.getMessage());
			try {
				socket.close();
			} catch (IOException e1) {
				log.warning("Unable to close connection: " + e1.getMessage());
			}
			return;
		}
	}

	private void checkConnection() throws IOException {
		while (true) {
			login = messager.readLogin();
			if (clientMap.contains(login)) {
				messager.sendError("Already connected");
			} else {
				break;
			}
		}
	}

	private void createObserver() {
		FilledObserver observer = new FilledObserver(login) {
			@Override
			public void onFilled(Response response) {
				if (clientMap.contains(login)) {
					log.info(String
							.format("Send response to client: client=%s about order: orderID=%d",
									login, response.getOrderID()));
					try {
						messager.sendResponse(response);
					} catch (IOException e) {
					}
				} else {
					log.info(String
							.format("Add delayed response to client: client=%s about order: orderID=%d",
									login, response.getOrderID()));
					serviceContainer.addDelayedResponse(response);
				}
			}
		};

		serviceContainer.addObserver(observer);

	}

	private void sendDelayedResponses(ObjectOutputStream out)
			throws IOException {
		LinkedList<Response> delayedResponses = serviceContainer
				.getDelayedResponses(login);
		if (delayedResponses != null) {
			for (Response response : delayedResponses)
				messager.sendResponse(response);
			log.info(String.format("Sending %d delayed responses to client=%s",
					delayedResponses.size(), login));
		} else {
			log.info(String.format("No delayed responses for client=%s", login));
		}
		messager.sendSuccessfullLoginMessage();
	}

	private void listenClient() throws IOException {
		OrderVerifier orderVerifier = new OrderVerifier();
		OrderExecutor orderExecutor = new OrderExecutor(messager,
				serviceContainer, orderVerifier);
		while (true) {
			try {
				Object message = messager.readMessage();
				int orderID = generateOrderID();
				boolean isExecuted = orderExecutor.execute(login, message,
						orderID);
				if (isExecuted == false)
					log.info("Bad client request" + message.toString());
			} catch (SocketException | DisconnectException closeException) {
				disconnectClient();
				return;
			}

		}
	}

	private void disconnectClient() {
		try {
			in.close();
			out.close();
			socket.close();
			clientMap.remove(login);
			log.info(String.format("Client disconnected: client=%s", login));
		} catch (IOException e) {
			log.warning(String.format("Unable to disconnect: client=%s", login));
		}
	}

}
