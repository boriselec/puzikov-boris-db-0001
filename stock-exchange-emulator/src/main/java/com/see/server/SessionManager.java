package com.see.server;

import java.io.IOException;
import java.util.HashMap;

import com.see.common.exception.NoLoginException;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessagerAPI;

public class SessionManager {

	class Disconnector implements Runnable {

		public Disconnector(String clientName, Thread thread) {
			this.clientName = clientName;
			this.wrappedThread = thread;
		}

		String clientName;
		Thread wrappedThread;

		@Override
		public void run() {
			try {
				wrappedThread.join();
			} catch (InterruptedException ignored) {
			}
			clientMap.remove(clientName);
		}

	}

	private HashMap<String, Thread> clientMap = new HashMap<>();

	private String getLogin(TradingMessagerAPI tradingMessager)
			throws NoLoginException {
		String clientLogin;
		try {
			clientLogin = tradingMessager.readLogin();
		} catch (IOException e) {
			throw new NoLoginException("Unable to read client login name");
		}
		return clientLogin;
	}

	public ClientSession getClientSession(ServiceContainer serviceContainer,
			TradingMessagerAPI tradingMessager, ResponseManager responseManager)
			throws NoLoginException {
		String clientLogin = getLogin(tradingMessager);
		if (clientMap.containsKey(clientLogin))
			throw new NoLoginException("Server: Already connected");
		else {
			ClientSession result = new ClientSession(clientLogin,
					serviceContainer, tradingMessager, responseManager);
			return result;
		}
	}

	public void startThread(ClientSession client) {
		Thread clientThread = new Thread(client);
		Thread disconnector = new Thread(new Disconnector(client.getName(),
				clientThread));
		clientMap.put(client.getName(), clientThread);
		disconnector.start();
		clientThread.start();
	}

	public void close() {
		for (String clientString : clientMap.keySet()) {
			Thread clientThread = clientMap.remove(clientString);
			clientThread.interrupt();
		}
	}
}
