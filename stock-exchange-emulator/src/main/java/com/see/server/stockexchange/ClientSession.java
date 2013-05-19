package com.see.server.stockexchange;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.see.domain.ClientResponse;
import com.see.domain.OrderBookResponse;
import com.see.domain.OrderVerifier;
import com.see.domain.ResponseManager;
import com.see.server.business.ServiceContainer;

public class ClientSession implements Runnable {
	private static Logger log = Logger.getLogger(ClientSession.class.getName());

	private String clientName;
	private HashSet<String> clientMap;
	private ServiceContainer serviceContainer;

	private TradingMessager messager;
	private ResponseManager responseManager;

	private FilledObserver observer;

	public ClientSession(ServiceContainer serviceContainer,
			HashSet<String> clientMap2, TradingMessager tradingMessager,
			ResponseManager responseManager) throws IOException {
		this.serviceContainer = serviceContainer;
		this.clientMap = clientMap2;
		this.messager = tradingMessager;
		this.responseManager = responseManager;
	}

	@Override
	public void run() {

		try {
			checkConnection();
			sendDelayedResponses();

			clientMap.add(clientName);
			createObserver();
			log.info(String
					.format("New client connected: login=%s", clientName));
			listenClient();
		} catch (IOException e) {
			e.printStackTrace();
			log.warning("IO error: " + e.getMessage());
			try {
				messager.sendError("Server IO error: " + e.getMessage());
			} catch (IOException e1) {
				log.warning("Unable to send error message");
			}
		}
	}

	private void createObserver() {

		observer = new FilledObserver(clientName) {
			@Override
			public void onFilled(OrderBookResponse orderBookResponse) {
				System.out.println(orderBookResponse);
				ClientResponse response = responseManager.createResponse(
						orderBookResponse, clientName);
				if (clientMap.contains(clientName)) {
					log.info(String
							.format("Send response to client: client=%s about order: orderID=%s",
									clientName, response.getOrderID()
											.toString()));
					try {
						messager.sendResponse(response);
					} catch (IOException e) {
						log.info(String
								.format("Unable to send response to client: client=%s about order: orderID=%s",
										clientName, response.getOrderID()
												.toString()));
					}
				} else {
					log.info(String
							.format("Add delayed response to client: client=%s about order: orderID=%s",
									clientName, response.getOrderID()
											.toString()));
					serviceContainer.addDelayedResponse(response);
				}
			}
		};
		serviceContainer.addObserver(observer);
	}

	private void checkConnection() throws IOException {
		while (true) {
			clientName = messager.readLogin();
			if (clientMap.contains(clientName)) {
				messager.sendError("Server: Already connected");
			} else {
				break;
			}
		}
	}

	private void sendDelayedResponses() throws IOException {
		LinkedList<ClientResponse> delayedResponses = serviceContainer
				.getDelayedResponses(clientName);
		if (delayedResponses != null) {
			for (ClientResponse response : delayedResponses)
				messager.sendResponse(response);
			log.info(String.format("Sending %d delayed responses to client=%s",
					delayedResponses.size(), clientName));
		} else {
			log.info(String.format("No delayed responses for client=%s",
					clientName));
		}
		messager.sendSuccessfullLoginMessage();
	}

	private void listenClient() throws IOException {
		OrderVerifier orderVerifier = new OrderVerifier();
		OrderExecutor orderExecutor = new OrderExecutor(messager,
				serviceContainer, orderVerifier);
		while (true) {
			try {
				Object message = messager.readOrder();
				boolean isExecuted = orderExecutor.execute(clientName, message);
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
			serviceContainer.removeObserver(observer);
			messager.disconnect();
			clientMap.remove(clientName);
			log.info(String
					.format("Client disconnected: client=%s", clientName));
		} catch (IOException e) {
			log.warning(String.format("Unable to disconnect: client=%s",
					clientName));
		}
	}

}