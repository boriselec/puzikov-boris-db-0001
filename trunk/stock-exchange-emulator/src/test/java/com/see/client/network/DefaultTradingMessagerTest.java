package com.see.client.network;

import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;
import org.mockito.Mockito;

import com.see.common.exception.LoginException;
import com.see.common.network.NetworkMessager;

public class DefaultTradingMessagerTest {

	@Test(expected = LoginException.class)
	public void test_shouldThrowException_whenReadBadDelayedResponse()
			throws LoginException, IOException {
		NetworkMessager networkMessager = mock(NetworkMessager.class);
		Mockito.when(networkMessager.read()).thenReturn("Bad Value");
		TradingMessager tradingMessager = new DefaultTradingMessager(
				networkMessager);
		tradingMessager.readDelayedResponses();

	}

}
