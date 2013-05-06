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
		MockClient client = new MockClient();
		int response;
		try {
			response = (int) client.run("login");
		} catch (NoLoginException e) {
			throw e;
		}
		return response;
	}

	public int sendOrder(Order order) {
		// TEST
		MockClient client = new MockClient();
		Response response = null;
		try {
			response = (Response) client.run(order);
		} catch (NoLoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(response);
		final Response response2 = response;
		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				notifyObservers(response2);
			}
		}.start();
		return response.getOrderID();

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
