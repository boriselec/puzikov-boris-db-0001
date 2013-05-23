package com.see.server;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import com.see.common.message.TradeResponse;

public class DelayedResponsesContainer {
	private ConcurrentHashMap<String, LinkedList<TradeResponse>> delayedResponses = new ConcurrentHashMap<>();

	public void addDelayedResponse(String client, TradeResponse response) {
		if (delayedResponses.containsKey(client) == false)
			delayedResponses.put(client, new LinkedList<TradeResponse>());
		delayedResponses.get(client).add(response);
	}

	public LinkedList<TradeResponse> getDelayedResponses(String client) {
		if (delayedResponses.containsKey(client) == false)
			return null;
		LinkedList<TradeResponse> result = delayedResponses.remove(client);
		delayedResponses.remove(client);
		return result;
	}
}
