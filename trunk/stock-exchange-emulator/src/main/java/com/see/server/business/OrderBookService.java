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

	public OrderBookService() {
		orderBook = new OrderBookImpl();
		observers = new HashMap<String, TradeListener>();
	}

	private OrderBook orderBook;
	private Map<String, TradeListener> observers;

	@Override
	public void sendOrder(Order order) {
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
	public void addObserver(TradeListener observer) {
		observers.put(observer.getClientName(), observer);
	}

	@Override
	public void removeObserver(TradeListener observer) {
		observers.remove(observer);
	}

	private void notifyObservers(Trade trade) {
		String bidClient = trade.getBid().getClientName();
		String offerClient = trade.getOffer().getClientName();
		observers.get(bidClient).onTrade(trade);
		observers.get(offerClient).onTrade(trade);
	}

}
