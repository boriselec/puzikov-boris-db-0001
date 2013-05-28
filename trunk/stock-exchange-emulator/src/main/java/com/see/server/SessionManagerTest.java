package com.see.server;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.see.common.exception.NoLoginException;
import com.see.common.utils.ResponseManager;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessager;

public class SessionManagerTest {

	private final SessionManager sessionManager = new SessionManager();
	ServiceContainer serviceContainer = mock(ServiceContainer.class);
	TradingMessager tradingMessager = mock(TradingMessager.class);
	ResponseManager responseManager = mock(ResponseManager.class);
	DelayedResponsesContainer delayedResponsesContainer = mock(DelayedResponsesContainer.class);

	@Test(expected = NoLoginException.class)
	public void testShouldThrowExceptionWhenSessionExists()
			throws NoLoginException {

		ClientSession clientSession = sessionManager.getClientSession("test",
				serviceContainer, tradingMessager, responseManager,
				delayedResponsesContainer);
		sessionManager.startThread(clientSession);

		sessionManager.getClientSession("test", serviceContainer,
				tradingMessager, responseManager, delayedResponsesContainer);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testShouldThrowExceptionStartedAlreadyRunnedSession()
			throws NoLoginException {

		ClientSession clientSession = sessionManager.getClientSession("test",
				serviceContainer, tradingMessager, responseManager,
				delayedResponsesContainer);
		sessionManager.startThread(clientSession);
		sessionManager.startThread(clientSession);

	}
}
