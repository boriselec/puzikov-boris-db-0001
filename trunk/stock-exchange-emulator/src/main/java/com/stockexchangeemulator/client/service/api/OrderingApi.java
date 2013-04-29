package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;

public interface OrderingApi {

	public void login() throws NoLoginException;

	public void sendOrder(Order order, ResponseListenerApi listener)
			throws BadOrderException;

}
