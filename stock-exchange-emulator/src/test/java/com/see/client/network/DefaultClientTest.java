package com.see.client.network;

import org.junit.Test;

import com.see.common.exception.NoLoginException;

public class DefaultClientTest {

	private Client client = new DefaultClient();

	@Test(expected = NoLoginException.class)
	public void test_shouldThrowException_whenEmptyLogin()
			throws NoLoginException {
		client.login(null);
	}

}
