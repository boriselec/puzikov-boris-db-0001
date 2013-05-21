package com.see.server.business;

import java.util.HashMap;
import java.util.LinkedList;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;
import com.see.server.FilledObserver;
import com.see.server.TradingServise;

public class ServiceContainer implements TradingServise {
	private String[] tickerSymbols;
	private HashMap<String, OrderBookService> orderBookContainer = new HashMap<>();
	private HashMap<String, LinkedList<ClientResponse>> delayedResponses = new HashMap<>();

	public ServiceContainer(String[] tickers) {
		this.tickerSymbols = tickers;

		for (String ticker : tickers) {
			orderBookContainer.put(ticker, new OrderBookService());
		}
	}

	@Override
	public void addDelayedResponse(ClientResponse response) {
		String login = response.getLogin();
		if (delayedResponses.containsKey(login) == false)
			delayedResponses.put(login, new LinkedList<ClientResponse>());
		delayedResponses.get(login).add(response);
	}

	@Override
	public LinkedList<ClientResponse> getDelayedResponses(String login) {
		if (delayedResponses.containsKey(login) == false)
			return null;
		LinkedList<ClientResponse> result = delayedResponses.remove(login);
		delayedResponses.remove(login);
		return result;
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
	public void removeObserver(FilledObserver observer) {
		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).removeObserver(observer);
	}

	public String[] getTickerSymbols() {
		return tickerSymbols;
	}

	public void cancel(CancelOrder order) {
		this.orderBookContainer.get(order.getStockName())
				.sendCancelOrder(order);
	}
}
