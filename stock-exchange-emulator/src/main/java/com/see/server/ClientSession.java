package com.see.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.logging.Logger;

import com.see.common.domain.Trade;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.DisconnectException;
import com.see.common.message.TradeResponse;
import com.see.common.utils.OrderExecutor;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessager;

public class ClientSession implements Runnable {
	private static Logger log = Logger.getLogger(ClientSession.class.getName());

	private final String clientName;
	private final ServiceContainer serviceContainer;

	private final TradingMessager messager;
	private final ResponseManager responseManager;
	private final DelayedResponsesContainer delayedResponsesContainer;

	private TradeListener listener;

	private boolean isConnected = true;

	public ClientSession(String clientName, ServiceContainer serviceContainer,
			TradingMessager tradingMessager, ResponseManager responseManager,
			DelayedResponsesContainer delayedResponsesContainer) {
		this.clientName = clientName;
		this.serviceContainer = serviceContainer;
		this.messager = tradingMessager;
		this.responseManager = responseManager;
		this.delayedResponsesContainer = delayedResponsesContainer;
	}

	@Override
	public void run() {

		try {
			sendDelayedResponses();

			createListener();
			listenSocket();

		} catch (IOException e) {
			log.warning("IO error: " + e.getMessage());
			try {
				messager.sendError("Server IO error: " + e.getMessage());
			} catch (IOException e1) {
				log.warning("Unable to send error message");
			}
		}
	}

	private void sendDelayedResponses() throws IOException {
		LinkedList<TradeResponse> delayedResponses = delayedResponsesContainer
				.getDelayedResponses(clientName);

		if (delayedResponses != null)
			for (TradeResponse response : delayedResponses)
				messager.sendResponse(response);

		messager.sendOkMessage();
	}

	private void listenSocket() throws IOException {
		OrderExecutor orderExecutor = new OrderExecutor(messager,
				serviceContainer);
		while (true) {
			try {
				Object message = messager.readOrder();
				orderExecutor.execute(clientName, message);
			} catch (BadOrderException e) {
				log.warning("Bad order request");
				messager.sendBadOrderID(null);
			} catch (SocketException | DisconnectException closeException) {
				disconnectClient();
				isConnected = false;
				return;
			}

		}
	}

	private void disconnectClient() {
		try {
			serviceContainer.removeObserver(listener);
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

	private void createListener() {

		listener = new TradeListener(clientName) {
			@Override
			public void onTrade(Trade trade) {
				TradeResponse response = responseManager.createResponse(trade,
						clientName);
				if (isConnected == true) {
					try {
						messager.sendResponse(response);
					} catch (IOException e) {
						delayedResponsesContainer.addDelayedResponse(
								clientName, response);
					}
				} else {
					delayedResponsesContainer.addDelayedResponse(clientName,
							response);
				}
			}
		};
		serviceContainer.addObserver(listener);
	}

}