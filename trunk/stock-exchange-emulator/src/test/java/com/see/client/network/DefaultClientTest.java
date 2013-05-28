package com.see.client.network;

import org.junit.Test;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.LoginException;
import com.see.common.message.OrderRequest;

public class DefaultClientTest {

	private Client client = new DefaultClient();

	@Test(expected = LoginException.class)
	public void testShouldThrowExceptionWhenEmptyLogin()
			throws LoginException {
		client.login(null);
	}

	@Test(expected = LoginException.class)
	public void testShouldThrowExceptionWhenConnectingToAlreadyConnected()
			throws LoginException {
		client.login("test");
		client.login("test");
	}

	@Test(expected = LoginException.class)
	public void testShouldThrowExceptionWhenSendOrderToNotConnected()
			throws LoginException, BadOrderException {
		OrderRequest order = null;
		client.sendOrder(order);
	}

}
