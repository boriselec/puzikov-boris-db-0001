package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Order;

public interface OrderingApi {

	public void sendOrder(Order order);

	public void addObserver(FilledObserver observer);

	public void removeObserver(FilledObserver observer);

}
