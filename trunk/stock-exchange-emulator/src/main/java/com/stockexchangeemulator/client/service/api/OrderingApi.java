package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;

public interface OrderingApi {
	public void login(String loginName) throws NoLoginException;

	public int sendOrder(Order order) throws BadOrderException,
			NoLoginException;

	public void addObserver(OrderObserver observer);

	public void removeObserver(OrderObserver observer);

}
