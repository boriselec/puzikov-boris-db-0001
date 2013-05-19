package com.see.server.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.see.common.domain.Order;
import com.see.common.domain.OrderBookResponse;
import com.see.server.FilledObserver;

public class OrderBookService {
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
		LinkedList<OrderBookResponse> responses = null;
		try {
			Order order = queue.take();
			responses = orderBook.proceedOrder(order);
			log.info(String.format(
					"Order: orderID=%s proceeded %d responses generated", order
							.getCancelingOrderID().toString(), responses.size()));
		} catch (InterruptedException ignoredException) {
		}

		for (OrderBookResponse response : responses)
			notifyObservers(response);
	}

	public void addObserver(FilledObserver observer) {
		observers.put(observer.getClientName(), observer);
	}

	public void removeObserver(FilledObserver observer) {
		observers.remove(observer);
	}

	private void notifyObservers(OrderBookResponse response) {
		for (String to : response.getTo())
			observers.get(to).onFilled(response);
	}
}
