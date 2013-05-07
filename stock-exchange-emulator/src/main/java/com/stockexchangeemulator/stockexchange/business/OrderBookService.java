package com.stockexchangeemulator.stockexchange.business;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.WrappedOrder;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class OrderBookService implements OrderingApi {
	private static Logger log = Logger.getLogger(OrderBook.class.getName());

	public OrderBookService() {
		queue = new LinkedBlockingQueue<WrappedOrder>();
		orderBook = new OrderBook();
		observers = new HashMap<Integer, FilledObserver>();
		runFilling();
	}

	private LinkedBlockingQueue<WrappedOrder> queue;
	private OrderBook orderBook;
	private Map<Integer, FilledObserver> observers;

	public void sendOrder(WrappedOrder order) {
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
			WrappedOrder order = queue.take();
			responses = orderBook.proceedOrder(order);
			log.info(String.format(
					"Order: orderID=%d proceeded %d responses generated", order
							.getOrder().getOrderID(), responses.size()));
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

	public void notifyObservers(Response response) {
		observers.get(response.getClientID()).onFilled(response);
	}
}
