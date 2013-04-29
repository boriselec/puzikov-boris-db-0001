package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Order;

public interface OrderingApi {

	public void sendOrder(Order order, FilledListenerApi listener);

}
