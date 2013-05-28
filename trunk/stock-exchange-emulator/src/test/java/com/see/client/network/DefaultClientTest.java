package com.see.client.network;

import org.junit.Test;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.NoLoginException;
import com.see.common.message.OrderRequest;

public class DefaultClientTest {

	private Client client = new DefaultClient();

	@Test(expected = NoLoginException.class)
	public void testShouldThrowExceptionWhenEmptyLogin()
			throws NoLoginException {
		client.login(null);
	}

	@Test(expected = NoLoginException.class)
	public void testShouldThrowExceptionWhenConnectingToAlreadyConnected()
			throws NoLoginException {
		client.login("test");
		client.login("test");
	}

	@Test(expected = NoLoginException.class)
	public void testShouldThrowExceptionWhenSendOrderToNotConnected()
			throws NoLoginException, BadOrderException {
		OrderRequest order = null;
		client.sendOrder(order);
	}

}
