package com.see.server.stockexchange;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Logger;

import com.see.domain.NetworkMessager;
import com.see.domain.ObjectStreamMessager;
import com.see.domain.ResponseManager;
import com.see.server.business.ServiceContainer;

public class StockExchange {
	private static Logger log = Logger.getLogger(StockExchange.class.getName());
	private ServerSocket serverSocket;
	private final static int DEFAULT_PORT = 2006;
	private static final int DEFAULT_QUEUE_LENGTH = 100;
	private ServiceContainer serviceContainer;
	private HashSet<String> clientMap = new HashSet<>();

	public StockExchange(ServiceContainer container) {
		this.serviceContainer = container;
	}

	private void runServer() throws IOException {
		serverSocket = new ServerSocket(DEFAULT_PORT, DEFAULT_QUEUE_LENGTH);
		log.info("Stock exchange started");
		while (true) {
			Socket newClientSocket = serverSocket.accept();
			ResponseManager responseManager = new ResponseManager();
			NetworkMessager networkMessager = new ObjectStreamMessager();
			TradingMessager tradingMessager = new TradingMessagerImpl(
					networkMessager);
			tradingMessager.connect(newClientSocket);

			Thread clientThread = new Thread(new ClientSession(
					serviceContainer, clientMap, tradingMessager,
					responseManager));
			clientThread.start();
		}
	}

	public static void main(String[] args) throws IOException {
		final String[] tickerSymbols = { "AAPL", "MCD", "IBM", "MSFT", "PG" };
		ServiceContainer container = new ServiceContainer(tickerSymbols);
		StockExchange stockExchange = new StockExchange(container);
		stockExchange.runServer();
	}
}
