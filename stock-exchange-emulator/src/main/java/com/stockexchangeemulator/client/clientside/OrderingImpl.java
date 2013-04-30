package com.stockexchangeemulator.client.clientside;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;

public class OrderingImpl implements OrderingApi {

	public OrderingImpl(OrderObserver... observers) {
		if (observers == null)
			this.observers = new ArrayList();
		else
			this.observers = new ArrayList(Arrays.asList(observers));
	}

	private List<OrderObserver> observers;

	public int login() throws NoLoginException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int sendOrder(Order order) throws BadOrderException {
		// TODO Auto-generated method stub
		return 0;

	}

	public void addObserver(OrderObserver observer) {
		// TODO Auto-generated method stub

	}

	public void removeObserver(OrderObserver observer) {
		// TODO Auto-generated method stub

	}

	public void notifyObservers() {
		// TODO Auto-generated method stub

	}

}
