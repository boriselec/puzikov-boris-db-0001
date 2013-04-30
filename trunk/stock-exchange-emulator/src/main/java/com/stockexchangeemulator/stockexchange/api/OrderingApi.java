package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.stockexchange.serverside.FilledObserver;

public interface OrderingApi {

	public void sendOrder(Order order);

	public void addObserver(FilledObserver observer);

	public void removeObserver(FilledObserver observer);

	public void notify(int clientID);

}
