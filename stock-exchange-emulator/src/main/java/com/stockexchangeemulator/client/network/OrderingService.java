package com.stockexchangeemulator.client.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class OrderingService implements OrderingApi {

	public OrderingService(OrderObserver... observers) {
		if (observers == null)
			this.observers = new ArrayList<OrderObserver>();
		else
			this.observers = new ArrayList<OrderObserver>(
					Arrays.asList(observers));
	}

	private List<OrderObserver> observers;

	public int login() throws NoLoginException {
		return 0;
	}

	public int sendOrder(Order order) {
		// TEST
		MockClient client = new MockClient();
		Response response = client.run(order);
		System.out.println(response);
		notifyObservers(response);
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
