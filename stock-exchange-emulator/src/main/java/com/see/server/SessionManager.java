package com.see.server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.see.common.exception.LoginException;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessager;

public class SessionManager {

	private Set<String> clientMap = new HashSet<>();
	private ExecutorService sessions = Executors.newCachedThreadPool();

	public ClientSession getClientSession(String clientLogin,
			ServiceContainer serviceContainer, TradingMessager tradingMessager,
			ResponseManager responseManager,
			DelayedResponsesContainer delayedResponsesContainer)
			throws LoginException {
		if (clientMap.contains(clientLogin))
			throw new LoginException("Server: Already connected");
		else {
			ClientSession result = new ClientSession(clientLogin,
					serviceContainer, clientMap, tradingMessager,
					responseManager, delayedResponsesContainer);
			return result;
		}
	}

	public void startThread(ClientSession client) {
		if (clientMap.contains(client.getName()))
			throw new IllegalArgumentException("Already runned");
		sessions.submit(client);
		clientMap.add(client.getName());
	}

	public void shutdown() {
		sessions.shutdownNow();
	}
}
