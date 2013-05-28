package com.see.server.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import com.see.common.domain.Order;
import com.see.common.domain.Trade;
import com.see.common.exception.CancelOrderException;
import com.see.server.TradeListener;

public class OrderBookService implements TradingService {
	private static Logger log = Logger.getLogger(OrderBookImpl.class.getName());

	public OrderBookService(OrderBook orderBook) {
		this.orderBook = orderBook;
		listeners = new HashMap<String, TradeListener>();
	}

	private OrderBook orderBook;
	private Map<String, TradeListener> listeners;

	@Override
	public void placeOrder(Order order) {
		orderBook.placeOrder(order);
		List<Trade> responses = orderBook.fillOrders();
		log.info(String.format(
				"Order: orderID=%s proceeded %d responses generated", order
						.getOrderID().toString(), responses.size()));

		for (Trade response : responses)
			notifyObservers(response);
	}

	@Override
	public void cancelOrder(UUID orderID) throws CancelOrderException {
		orderBook.cancelOrder(orderID);
	}

	@Override
	public void addListener(TradeListener listener) {
		listeners.put(listener.getClientName(), listener);
	}

	@Override
	public void removeListener(TradeListener listener) {
		listeners.remove(listener);
	}

	private void notifyObservers(Trade trade) {
		String bidClient = trade.getBid().getClientName();
		String offerClient = trade.getOffer().getClientName();
		listeners.get(bidClient).onTrade(trade);
		listeners.get(offerClient).onTrade(trade);
	}

}
