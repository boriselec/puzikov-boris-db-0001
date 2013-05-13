package com.stockexchangeemulator.stockexchange.business;

import java.util.HashMap;
import java.util.LinkedList;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.stockexchange.api.FilledObserver;
import com.stockexchangeemulator.stockexchange.api.OrderingApi;

public class ServiceContainer implements OrderingApi {
	private String[] tickerSymbols;
	private HashMap<String, OrderBookService> orderBookContainer = new HashMap<>();
	private HashMap<String, LinkedList<Response>> delayedResponses = new HashMap<>();

	public ServiceContainer(String[] tickers) {
		this.tickerSymbols = tickers;

		for (String ticker : tickers) {
			orderBookContainer.put(ticker, new OrderBookService());
		}
	}

	public void addDelayedResponse(Response response) {
		String login = response.getLogin();
		if (delayedResponses.containsKey(login) == false)
			delayedResponses.put(login, new LinkedList<Response>());
		delayedResponses.get(login).add(response);
	}

	public LinkedList<Response> getDelayedResponses(String login) {
		if (delayedResponses.containsKey(login) == false)
			return null;
		LinkedList<Response> result = delayedResponses.remove(login);
		delayedResponses.remove(login);
		return result;
	}

	public String[] getTickerSymbols() {
		return tickerSymbols;
	}

	@Override
	public void sendOrder(Order order) {
		this.orderBookContainer.get(order.getStockName()).sendOrder(order);
	}

	@Override
	public void addObserver(FilledObserver observer) {

		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).addObserver(observer);
	}

	@Override
	public void removeObserver(String login) {
		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).removeObserver(login);
	}
}
