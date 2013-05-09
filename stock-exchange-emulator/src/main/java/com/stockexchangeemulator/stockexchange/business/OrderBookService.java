package com.stockexchangeemulator.stockexchange.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class OrderBookService implements OrderingApi {
	private static Logger log = Logger.getLogger(OrderBook.class.getName());

	public OrderBookService() {
		queue = new LinkedBlockingQueue<Order>();
		orderBook = new OrderBook();
		observers = new HashMap<String, FilledObserver>();
		runFilling();
	}

	private LinkedBlockingQueue<Order> queue;
	private OrderBook orderBook;
	private Map<String, FilledObserver> observers;

	public void sendOrder(Order order) {
		queue.add(order);
	}

	public void runFilling() {
		new Thread() {
			public void run() {
				while (true) {
					fillOrderFromQueue();
				}
			}
		}.start();
	}

	private void fillOrderFromQueue() {
		LinkedList<Response> responses = null;
		try {
			Order order = queue.take();
			responses = orderBook.proceedOrder(order);
			log.info(String.format(
					"Order: orderID=%d proceeded %d responses generated",
					order.getCancelingOrderID(), responses.size()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for (Response response : responses)
			notifyObservers(response);
	}

	public void addObserver(FilledObserver observer) {
		observers.put(observer.getClientID(), observer);
	}

	public void removeObserver(FilledObserver observer) {
		observers.remove(observer);
	}

	private void notifyObservers(Response response) {
		observers.get(response.getLogin()).onFilled(response);
	}
}
