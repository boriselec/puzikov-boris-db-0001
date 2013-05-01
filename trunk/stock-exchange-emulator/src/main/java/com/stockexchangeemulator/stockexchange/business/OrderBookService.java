package com.stockexchangeemulator.stockexchange.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.WrappedOrder;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class OrderBookService implements OrderingApi {

	public OrderBookService() {
		queue = new LinkedList<WrappedOrder>();
		orderBook = new OrderBook();
		observers = new HashMap<Integer, FilledObserver>();
	}

	private List<WrappedOrder> queue;
	private OrderBook orderBook;
	private Map<Integer, FilledObserver> observers;

	public void sendOrder(WrappedOrder order) {
		synchronized (queue) {
			queue.add(order);
			notify();
		}
	}

	public void FillOrderFromQueue() {
		synchronized (queue) {
			while (queue.isEmpty()) {
				try {
					queue.wait();
				} catch (InterruptedException ignoredException) {
				}
				WrappedOrder order = queue.remove(0);
				HashSet<Response> responses = orderBook.proceedOrder(order);

				for (Response response : responses)
					notifyObservers(response);
			}
		}
	}

	public void addObserver(FilledObserver observer) {
		observers.put(observer.getClientID(), observer);
	}

	public void removeObserver(FilledObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers(Response response) {
		observers.get(response.getClientID()).onFilled(response);
	}
}
