package com.stockexchangeemulator.client.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class NetworkService implements OrderingApi {

	public NetworkService(OrderObserver... observers) {
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
		observers.add(observer);
	}

	public void removeObserver(OrderObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers(Response response) {
		for (OrderObserver observer : observers) {
			observer.onResponse(response);
		}
	}

}
