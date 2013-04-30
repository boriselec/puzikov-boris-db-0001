package com.stockexchangeemulator.stockexchange.business;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;
import com.stockexchangeemulator.stockexchange.serverside.FilledObserver;

public class OrderBookService implements OrderingApi {

	public OrderBookService() {
		queue = new LinkedList();
		orderBook = new OrderBook();
		observers = new ArrayList();
	}

	private LinkedList<Order> queue;
	private OrderBook orderBook;
	private List<FilledObserver> observers;

	public void sendOrder(Order order) {
		// TODO Auto-generated method stub

	}

	public void addObserver(FilledObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(FilledObserver observer) {
		observers.remove(observer);
	}

	public void notify(int clientID) {
		for (FilledObserver observer : observers)
			if (observer.getClientID() == clientID)
				observer.onFilled(new Response());
	}

}
