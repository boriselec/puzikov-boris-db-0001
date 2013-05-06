package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public interface OrderingApi {
	public int login() throws NoLoginException;

	public int sendOrder(Order order) throws BadOrderException,
			NoLoginException;

	public void addObserver(OrderObserver observer);

	public void removeObserver(OrderObserver observer);

	public void notifyObservers(Response response);

}
