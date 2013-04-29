package com.stockexchangeemulator.client.clientside;

import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.api.ResponseListenerApi;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;

public class OrderingImpl implements OrderingApi {

	public void login() throws NoLoginException {
		// TODO Auto-generated method stub

	}

	public void sendOrder(Order order, ResponseListenerApi listener)
			throws BadOrderException {
		// TODO Auto-generated method stub

	}

}
