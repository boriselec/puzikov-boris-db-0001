package com.see.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import com.see.common.exception.LoginException;
import com.see.server.network.TradingMessager;

public class LoginServiceTest {

	private final LoginService loginService = new LoginService();

	@SuppressWarnings("unchecked")
	@Test(expected = LoginException.class)
	public void testShouldThrowExceptionWhenReadBadLogin()
			throws LoginException, IOException {

		TradingMessager messager = mock(TradingMessager.class);
		when(messager.readLogin()).thenThrow(IOException.class);

		loginService.getLogin(messager);

	}

}
