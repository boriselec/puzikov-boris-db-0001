package com.stockexchangeemulator.stockexchange.stockexchange;

import java.util.HashMap;

import com.stockexchangeemulator.stockexchange.business.OrderBookService;

public class StockExchange {
	// TODO: define ticker symbols in file
	private final static String[] TICKER_SYMBOLS = { "AAPL", "MCD", "IBM",
			"MSFT", "PG" };
	private HashMap<String, OrderBookService> serviceContainer;
	private ClientMap clientMap;

	private HashMap<String, OrderBookService> createServiceContainer(
			String[] tickers) {
		HashMap<String, OrderBookService> result = new HashMap();

		for (String ticker : tickers) {
			result.put(ticker, new OrderBookService());
		}
		return result;
	}

	// TODO: remove
	public void testCycle() {
		serviceContainer = createServiceContainer(TICKER_SYMBOLS);
		clientMap = new ClientMap();
		for (String ticker : TICKER_SYMBOLS) {
			OrderBookService orderBookService = serviceContainer.get(ticker);
		}
	}
}
