package com.see.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import com.see.common.exception.NoLoginException;
import com.see.common.network.NetworkMessager;
import com.see.common.network.ObjectStreamMessager;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.DefaultTradingMessager;
import com.see.server.network.TradingMessager;

public class Server {
	private static Logger log = Logger.getLogger(Server.class.getName());
	private ServerSocket serverSocket;
	private final static int DEFAULT_PORT = 2006;
	private static final int DEFAULT_QUEUE_LENGTH = 100;
	private ServiceContainer serviceContainer;

	public Server(ServiceContainer container) {
		this.serviceContainer = container;
	}

	public void runServer() throws IOException {
		serverSocket = new ServerSocket(DEFAULT_PORT, DEFAULT_QUEUE_LENGTH);
		log.info("Stock exchange started");

		SessionManager sessionManager = new SessionManager();
		DelayedResponsesContainer delayedResponsesContainer = new DelayedResponsesContainer();
		ResponseManager responseManager = new ResponseManager();
		LoginService loginService = new LoginService();

		listenSocket(sessionManager, delayedResponsesContainer,
				responseManager, loginService);
	}

	private void listenSocket(SessionManager sessionManager,
			DelayedResponsesContainer delayedResponsesContainer,
			ResponseManager responseManager, LoginService loginService)
			throws IOException {
		while (true) {
			Socket newClientSocket = serverSocket.accept();

			NetworkMessager networkMessager = new ObjectStreamMessager(
					newClientSocket);
			TradingMessager tradingMessager = new DefaultTradingMessager(
					networkMessager);
			tradingMessager.connect();

			try {
				String loginString = loginService.getLogin(tradingMessager);
				ClientSession client = sessionManager.getClientSession(
						loginString, serviceContainer, tradingMessager,
						responseManager, delayedResponsesContainer);
				sessionManager.startThread(client);
			} catch (NoLoginException e) {
				tradingMessager.sendError(e.getMessage());
				log.info("Unable to get login. Reconnect. " + e.getMessage());
			}
		}
	}

	public static void main(String[] args) throws IOException {
		final String[] tickerSymbols = { "AAPL", "MCD", "IBM", "MSFT", "PG" };
		final float[] lastSessionPrices = { (float) 100.0, (float) 100.0,
				(float) 100.0, (float) 100.0, (float) 100.0 };
		ServiceContainer container = new ServiceContainer(tickerSymbols,
				lastSessionPrices);
		Server stockExchange = new Server(container);
		stockExchange.runServer();
	}
}
