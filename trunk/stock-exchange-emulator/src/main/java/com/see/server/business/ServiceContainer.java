package com.see.server.business;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;
import com.see.server.TradeListener;

public class ServiceContainer implements TradingService {
	private String[] tickerSymbols;
	private Map<String, OrderBookService> orderBookContainer = new HashMap<>();

	public ServiceContainer(String[] tickers, float[] lastSessionPrices) {
		if (tickers.length != lastSessionPrices.length)
			throw new IllegalArgumentException(
					"Tickers count must be equal prices count");

		this.tickerSymbols = tickers;

		for (int i = 0; i < tickers.length; i++) {
			orderBookContainer.put(tickers[i], new OrderBookService(
					new OrderBookImpl(lastSessionPrices[i])));
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
	public void addListener(TradeListener listener) {

		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).addListener(listener);
	}

	@Override
	public void removeListener(TradeListener listener) {
		for (String ticker : tickerSymbols)
			this.orderBookContainer.get(ticker).removeListener(listener);
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

			} catch (CancelOrderException ignored) {
			}
		throw new CancelOrderException(orderID, "No such order");
	}

}
