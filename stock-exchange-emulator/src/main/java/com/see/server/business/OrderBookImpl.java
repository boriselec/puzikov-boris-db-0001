package com.see.server.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.see.common.comparator.InverseOrderComparator;
import com.see.common.comparator.OrderComparator;
import com.see.common.domain.Trade;
import com.see.common.domain.OrderType;
import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;
import com.see.common.pricing.LastPriceMatcher;
import com.see.common.pricing.PriceComparator;
import com.see.common.pricing.PriceMatcher;

public class OrderBookImpl implements OrderBook {
	private static float DEFAULT_PRICE = (float) 100.0;

	public OrderBookImpl() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());

		this.mainMatcher = new LastPriceMatcher();
	}

	private TreeSet<Order> bidsOrderBook;
	private TreeSet<Order> offersOrderBook;

	private PriceMatcher mainMatcher;
	private float lastDealPrice = DEFAULT_PRICE;

	@Override
	public void placeOrder(Order order) {
		switch (order.getType()) {

		case BUY:
			bidsOrderBook.add(order);
			break;

		case SELL:
			offersOrderBook.add(order);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void cancelOrder(UUID orderID) throws CancelOrderException {

		for (Order order : bidsOrderBook) {
			if (order.getOrderID().equals(orderID)) {
				bidsOrderBook.remove(order);
				return;
			}
		}
		for (Order order : offersOrderBook) {
			if (order.getOrderID().equals(orderID)) {
				offersOrderBook.remove(order);
				return;
			}
		}

		// not found
		throw new CancelOrderException("No suck order");
	}

	private void removeFirst(OrderType type) {
		Set<Order> book;
		if (type == OrderType.BUY)
			book = bidsOrderBook;
		else
			book = offersOrderBook;
		Iterator<Order> iterator = book.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	private Order getFirst(Set<Order> orderBook) {
		Iterator<Order> iterator = orderBook.iterator();
		return iterator.next();
	}

	@Override
	public List<Trade> fillOrders() {
		List<Trade> responses = new LinkedList<>();

		while (true) {
			if (bidsOrderBook.size() == 0 || offersOrderBook.size() == 0)
				break;

			Order bid = getFirst(bidsOrderBook);
			float bidPrice = bid.getPrice();

			Order offer = getFirst(offersOrderBook);
			float offerPrice = offer.getPrice();

			if (PriceComparator.match(offerPrice, bidPrice)) {
				float dealPrice = mainMatcher.getPrice(offer, bid, lastDealPrice);
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
		removeFirst(order.getType());
		placeOrder(order.getPartiallyFilledOrder(sharesCount));
	}

	private void fullyFill(Order order) {
		removeFirst(order.getType());
	}

}
