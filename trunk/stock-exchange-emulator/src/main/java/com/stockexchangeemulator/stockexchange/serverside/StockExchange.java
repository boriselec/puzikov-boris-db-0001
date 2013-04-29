package com.stockexchangeemulator.stockexchange.serverside;

import com.stockexchangeemulator.stockexchange.business.ServiceContainer;

public class StockExchange {
	private final static String[] TICKER_SYMBOLS = { "AAPL", "MCD", "IBM",
			"MSFT", "PG" };
	private ServiceContainer container;
	private ClientMap clientMap;

	// TODO: remove
	public void testCycle() {
		container = new ServiceContainer(TICKER_SYMBOLS);
		clientMap = new ClientMap();
	}
}
