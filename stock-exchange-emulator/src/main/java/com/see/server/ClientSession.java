package com.see.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.OrderBookResponse;
import com.see.common.exception.DisconnectException;
import com.see.common.utils.OrderExecutor;
import com.see.common.utils.OrderVerifier;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessagerAPI;

public class ClientSession implements Runnable {
	private static Logger log = Logger.getLogger(ClientSession.class.getName());

	private final String clientName;
	private final ServiceContainer serviceContainer;

	private final TradingMessagerAPI messager;
	private final ResponseManager responseManager;

	private FilledObserver observer;

	private boolean isConnected = true;

	public ClientSession(String clientName, ServiceContainer serviceContainer,
			TradingMessagerAPI tradingMessager, ResponseManager responseManager) {
		this.clientName = clientName;
		this.serviceContainer = serviceContainer;
		this.messager = tradingMessager;
		this.responseManager = responseManager;
	}

	@Override
	public void run() {

		try {
			sendDelayedResponses();

			createObserver();
			listenSocket();

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

	private void sendDelayedResponses() throws IOException {
		LinkedList<ClientResponse> delayedResponses = serviceContainer
				.getDelayedResponses(clientName);
		if (delayedResponses != null) {
			for (ClientResponse response : delayedResponses)
				messager.sendResponse(response);
		}
		messager.sendOkMessage();
	}

	private void listenSocket() throws IOException {
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
				isConnected = false;
				return;
			}

		}
	}

	private void disconnectClient() {
		try {
			serviceContainer.removeObserver(observer);
			messager.disconnect();
			log.info(String
					.format("Client disconnected: client=%s", clientName));
		} catch (IOException e) {
			log.warning(String.format("Unable to disconnect: client=%s",
					clientName));
		}
	}

	public String getName() {
		return clientName;
	}

	private void createObserver() {

		observer = new FilledObserver(clientName) {
			@Override
			public void onFilled(OrderBookResponse orderBookResponse) {
				ClientResponse response = responseManager.createResponse(
						orderBookResponse, clientName);
				if (isConnected == true) {
					try {
						messager.sendResponse(response);
					} catch (IOException e) {
						serviceContainer.addDelayedResponse(response);
					}
				} else {
					serviceContainer.addDelayedResponse(response);
				}
			}
		};
		serviceContainer.addObserver(observer);
	}

}