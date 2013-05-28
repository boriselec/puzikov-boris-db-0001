package com.see.server;

import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.common.message.TradeResponse;

public class DelayedResponsesContainerTest extends TestCase {

	@Test
	public void testShouldStoreResponcesWhenAddedAndThenGetted() {
		DelayedResponsesContainer container = new DelayedResponsesContainer();

		UUID id = UUID.randomUUID();
		TradeResponse response = new TradeResponse(id, "Test", 0, 0);
		container.addDelayedResponse("test", response);

		java.util.List<TradeResponse> result = container
				.getDelayedResponses("test");
		assertEquals(result.size(), 1);
		assertEquals(result.get(0).getOrderID(), id);
	}
}
