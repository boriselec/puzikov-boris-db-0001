package com.see.server;

import java.util.LinkedList;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;

public interface TradingServise {

	public void sendOrder(Order order);

	public void addDelayedResponse(ClientResponse response);

	public LinkedList<ClientResponse> getDelayedResponses(String clientName);

	public void addObserver(FilledObserver observer);

	public void removeObserver(FilledObserver observer);

}
