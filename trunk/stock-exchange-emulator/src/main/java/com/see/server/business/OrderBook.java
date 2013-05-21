package com.see.server.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.UUID;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.CancelResponse;
import com.see.common.domain.ErrorResponse;
import com.see.common.domain.InverseOrderComparator;
import com.see.common.domain.Order;
import com.see.common.domain.OrderBookResponse;
import com.see.common.domain.OrderComparator;
import com.see.common.domain.Trade;
import com.see.common.domain.TradeOperation;
import com.see.common.domain.TradeOrder;
import com.see.common.pricing.LastPriceMatcher;
import com.see.common.pricing.PriceComparator;
import com.see.common.pricing.PriceMatcher;

public class OrderBook {
	private static float DEFAULT_PRICE = (float) 100.0;

	public OrderBook() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());

		this.mainMatcher = new LastPriceMatcher();
	}

	private TreeSet<TradeOrder> bidsOrderBook;
	private TreeSet<TradeOrder> offersOrderBook;

	private PriceMatcher mainMatcher;
	private float lastDealPrice = DEFAULT_PRICE;

	public LinkedList<OrderBookResponse> proceedOrder(Order order) {
		if (order instanceof CancelOrder) {
			return removeOrder((CancelOrder) order);
		} else {
			addOrder((TradeOrder) order);
			return fillOrders();
		}
	}

	public LinkedList<OrderBookResponse> removeOrder(CancelOrder cancelOrder) {
		LinkedList<OrderBookResponse> response = new LinkedList<>();
		UUID orderID = cancelOrder.getCancelingOrderID();

		for (TradeOrder order : bidsOrderBook) {
			if (order.getCancelingOrderID().equals(orderID)) {
				bidsOrderBook.remove(order);
				response.add(new CancelResponse(order));
				return response;
			}
		}
		for (TradeOrder order : offersOrderBook) {
			if (order.getCancelingOrderID().equals(orderID)) {
				offersOrderBook.remove(order);
				response.add(new CancelResponse(order));
				return response;
			}
		}

		// not found
		response.add(new ErrorResponse(cancelOrder,
				"Unable to cancel: no such order"));
		return response;
	}

	private void addOrder(TradeOrder order) {
		switch (order.getType()) {

		case BID:
			bidsOrderBook.add(order);
			break;

		case OFFER:
			offersOrderBook.add(order);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void removeFirst(TradeOperation type) {
		TreeSet<TradeOrder> book;
		if (type == TradeOperation.BID)
			book = bidsOrderBook;
		else
			book = offersOrderBook;
		Iterator<TradeOrder> iterator = book.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	private TradeOrder getBest(TreeSet<TradeOrder> orderBook) {
		Iterator<TradeOrder> iterator = orderBook.iterator();
		return iterator.next();
	}

	private LinkedList<OrderBookResponse> fillOrders() {
		LinkedList<OrderBookResponse> responses = new LinkedList<>();

		while (true) {
			if (bidsOrderBook.size() == 0 || offersOrderBook.size() == 0)
				break;

			TradeOrder bid = getBest(bidsOrderBook);
			float bidPrice = bid.getPrice();

			TradeOrder offer = getBest(offersOrderBook);
			float offerPrice = offer.getPrice();

			if (PriceComparator.match(offerPrice, bidPrice)) {
				float dealPrice = mainMatcher.match(offer, bid, lastDealPrice);
				lastDealPrice = dealPrice;

				OrderBookResponse dealResponse = fill(bid, offer, dealPrice);
				responses.add(dealResponse);
			} else
				break;
		}
		return responses;
	}

	private OrderBookResponse fill(TradeOrder order1, TradeOrder order2,
			float dealPrice) {
		int order1SharesCount = order1.getSharesCount();
		int order2SharesCount = order2.getSharesCount();

		if (order1SharesCount == order2SharesCount) {
			fullyFill(order1, dealPrice);
			fullyFill(order2, dealPrice);
		} else if (order1SharesCount > order2SharesCount) {
			partiallyFill(order1, dealPrice, order2SharesCount);
			fullyFill(order2, dealPrice);

		} else { // if (order1SharesCount < order2SharesCount)
			fullyFill(order1, dealPrice);
			partiallyFill(order2, dealPrice, order1SharesCount);

		}
		return new Trade(order1, order2, dealPrice, Math.min(order1SharesCount,
				order2SharesCount));
	}

	private void partiallyFill(TradeOrder order, float price, int sharesCount) {
		order.partliallyFill(price, sharesCount);
	}

	private void fullyFill(TradeOrder order, float price) {
		order.fullyFill(price);
		removeFirst(order.getType());
	}

}
