package com.see.server.business;

import java.util.HashMap;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;
import com.see.server.TradeListener;

public class ServiceContainer implements TradingService {
	private String[] tickerSymbols;
	private HashMap<String, OrderBookService> orderBookContainer = new HashMap<>();

	public ServiceContainer(String[] tickers) {
		this.tickerSymbols = tickers;

		for (String ticker : tickers) {
			orderBookContainer.put(ticker, new OrderBookService(
					new OrderBookImpl()));
		}
	}

	@Override
	public void placeOrder(Order order) {
		if (orderBookContainer.containsKey(order.getStock()))
			this.orderBookContainer.get(order.getStock()).placeOrder(order);
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void addObserver(TradeListener observer) {

		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).addObserver(observer);
	}

	@Override
	public void removeObserver(TradeListener observer) {
		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).removeObserver(observer);
	}

	public String[] getTickerSymbols() {
		return tickerSymbols;
	}

	@Override
	public void cancelOrder(UUID orderID) throws CancelOrderException {
		for (String stock : tickerSymbols)
			try {
				this.orderBookContainer.get(stock).cancelOrder(orderID);
				return;

			} catch (CancelOrderException e) {
			}
		throw new CancelOrderException("No such order");
	}

}
