package com.see.server;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.see.common.message.TradeResponse;

public class DelayedResponsesContainer {
	private ConcurrentHashMap<String, List<TradeResponse>> delayedResponses = new ConcurrentHashMap<>();

	public void addDelayedResponse(String client, TradeResponse response) {
		if (delayedResponses.containsKey(client) == false)
			delayedResponses.put(client, new LinkedList<TradeResponse>());
		delayedResponses.get(client).add(response);
	}

	public List<TradeResponse> getDelayedResponses(String client) {
		if (delayedResponses.containsKey(client) == false)
			return null;
		List<TradeResponse> result = delayedResponses.remove(client);
		delayedResponses.remove(client);
		return result;
	}
}
