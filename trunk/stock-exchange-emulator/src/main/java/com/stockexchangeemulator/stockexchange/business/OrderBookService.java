package com.stockexchangeemulator.stockexchange.business;

import java.util.LinkedList;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.stockexchange.api.FilledListenerApi;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class OrderBookService implements OrderingApi {
	LinkedList<Order> queue;
	OrderBook orderBook;

	public void sendOrder(Order order, FilledListenerApi listener) {
		// TODO Auto-generated method stub

	}
}
