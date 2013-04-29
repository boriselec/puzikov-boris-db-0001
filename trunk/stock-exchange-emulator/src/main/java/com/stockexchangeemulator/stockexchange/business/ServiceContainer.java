package com.stockexchangeemulator.stockexchange.business;

import java.util.HashMap;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.stockexchange.api.FilledListenerApi;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class ServiceContainer implements OrderingApi {
	public ServiceContainer(String[] tickerSymbols) {
	}

	private HashMap<String, OrderBookService> container;

	public void sendOrder(Order order, FilledListenerApi listener) {
		// TODO Auto-generated method stub

	}
}
