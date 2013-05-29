package com.see.server.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.see.common.comparator.BidComparator;
import com.see.common.comparator.OfferComparator;
import com.see.common.domain.Order;
import com.see.common.domain.OrderType;
import com.see.common.domain.Trade;
import com.see.common.exception.CancelOrderException;
import com.see.common.pricing.LastPriceMatcher;
import com.see.common.pricing.MatchingEngine;
import com.see.common.pricing.PriceComparator;

public class OrderBookImpl implements OrderBook {

	public OrderBookImpl(float lastDealPrice) {
		this.lastDealPrice = lastDealPrice;

		precision = (float) 0.001;
		bidsOrderBook = new TreeSet<>(new BidComparator(precision));
		offersOrderBook = new TreeSet<>(new OfferComparator(precision));

		PriceComparator priceComparator = new PriceComparator(precision);
		this.mainMatcher = new LastPriceMatcher(priceComparator);
	}

	private final float precision;
	private Set<Order> bidsOrderBook;
	private Set<Order> offersOrderBook;

	private MatchingEngine mainMatcher;

	private float lastDealPrice;

	@Override
	public void placeOrder(Order order) {
		switch (order.getType()) {

		case BUY:
			bidsOrderBook.add(order);
			break;

		case SELL:
			offersOrderBook.add(order);
			break;
		}
	}

	@Override
	public void cancelOrder(UUID orderID) throws CancelOrderException {

		for (Order order : bidsOrderBook) {
			if (order.getOrderID().equals(orderID)) {
				removeOrder(order);
				return;
			}
		}
		for (Order order : offersOrderBook) {
			if (order.getOrderID().equals(orderID)) {
				removeOrder(order);
				return;
			}
		}

		// not found
		throw new CancelOrderException(orderID, "No such order");
	}

	private void removeOrder(Order order) {
		removeBest(order.getType());
		// if (order.getType() == OrderType.BUY)
		// System.out.println(bidsOrderBook.remove(order));

		// else
		// System.out.println(offersOrderBook.remove(order));
	}

	private void removeBest(OrderType type) {
		Set<Order> book = (type == OrderType.BUY) ? (bidsOrderBook)
				: (offersOrderBook);
		Iterator<Order> iterator = book.iterator();
		iterator.next();
		iterator.remove();
	}

	private Order getBest(Set<Order> orderBook) {
		Iterator<Order> iterator = orderBook.iterator();
		return iterator.next();
	}

	@Override
	public List<Trade> fillOrders() {
		List<Trade> responses = new LinkedList<>();

		while (true) {
			if (bidsOrderBook.size() == 0 || offersOrderBook.size() == 0)
				break;

			Order bid = getBest(bidsOrderBook);

			Order offer = getBest(offersOrderBook);

			if (mainMatcher.isMatch(bid, offer)) {
				float dealPrice = mainMatcher.getPrice(offer, bid,
						lastDealPrice);
				lastDealPrice = dealPrice;

				Trade dealResponse = fill(bid, offer, dealPrice);
				responses.add(dealResponse);
			} else
				break;
		}
		return responses;
	}

	private Trade fill(Order order1, Order order2, float dealPrice) {
		int order1SharesCount = order1.getQuantity();
		int order2SharesCount = order2.getQuantity();

		if (order1SharesCount == order2SharesCount) {
			fullyFill(order1);
			fullyFill(order2);
		} else if (order1SharesCount > order2SharesCount) {
			partiallyFill(order1, order2SharesCount);
			fullyFill(order2);

		} else { // if (order1SharesCount < order2SharesCount)
			fullyFill(order1);
			partiallyFill(order2, order1SharesCount);

		}
		return new Trade(order1, order2, dealPrice, Math.min(order1SharesCount,
				order2SharesCount));
	}

	private void partiallyFill(Order order, int sharesCount) {
		removeOrder(order);
		placeOrder(order.getPartiallyFilledOrder(sharesCount));
	}

	private void fullyFill(Order order) {
		removeOrder(order);
	}

}
