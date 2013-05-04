package com.stockexchangeemulator.client.network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class OrderingService implements OrderingApi {

	public OrderingService(OrderObserver... observers) {
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

	public int sendOrder(Order order) {
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

	public Order getOrder(String stockName, Operation operation, String type,
			String priceString, String sharesCountString)
			throws BadOrderException {

		float price;
		int sharesCount;
		if ("limit".equals(type)) {
			try {
				price = Float.parseFloat(priceString);
			} catch (NumberFormatException e) {
				throw new BadOrderException();
			}
			if (price <= 0)
				throw new BadOrderException(
						"Limit order should hava positive price");
		} else {
			price = (operation == Operation.BID) ? Float.POSITIVE_INFINITY
					: Float.NEGATIVE_INFINITY;
		}
		try {
			sharesCount = Integer.parseInt(sharesCountString);
		} catch (NumberFormatException e) {
			throw new BadOrderException();
		}
		if (sharesCount <= 0)
			throw new BadOrderException("Order should hava positive quantity");
		Order result = new Order(stockName, operation, sharesCount, price);
		return result;
	}

	public void sendCancelOrder(int orderID) {

	}

}
