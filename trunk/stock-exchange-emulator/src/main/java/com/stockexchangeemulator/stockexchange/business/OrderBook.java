package com.stockexchangeemulator.stockexchange.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.ConstantPriceMatcher;
import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.LastPriceMatcher;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.PriceComparator;
import com.stockexchangeemulator.domain.PriceMatcher;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBook {
	private static Logger log = Logger.getLogger(OrderBook.class.getName());

	public OrderBook() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());

		this.startupMatcher = new ConstantPriceMatcher(getAveragePrice());
		this.mainMatcher = new LastPriceMatcher();
	}

	private float getAveragePrice() {
		// TODO: return average price for all crossing orders
		return (float) 0.0;
	}

	private TreeSet<WrappedOrder> bidsOrderBook;
	private TreeSet<WrappedOrder> offersOrderBook;

	private PriceMatcher mainMatcher;
	private PriceMatcher startupMatcher;

	public LinkedList<Response> proceedOrder(WrappedOrder wrappedOrder) {
		addOrder(wrappedOrder);

		return fillOrders();
	}

	private void addOrder(WrappedOrder wrappedOrder) {
		switch (wrappedOrder.getOrder().getType()) {

		case BID:
			bidsOrderBook.add(wrappedOrder);
			break;

		case OFFER:
			offersOrderBook.add(wrappedOrder);
			break;
		}
	}

	private void removeOrder(WrappedOrder wrappedOrder) {
		switch (wrappedOrder.getOrder().getType()) {

		case BID:
			bidsOrderBook.remove(wrappedOrder);
			break;

		case OFFER:
			offersOrderBook.remove(wrappedOrder);
			break;
		}
	}

	private LinkedList<Response> fillOrders() {
		LinkedList<Response> responses = new LinkedList<>();

		while (true) {
			Iterator<WrappedOrder> bidIterator = bidsOrderBook.iterator();
			Iterator<WrappedOrder> offerIterator = offersOrderBook.iterator();
			if (bidIterator.hasNext() == false
					|| offerIterator.hasNext() == false)
				break;

			WrappedOrder bid = bidIterator.next();
			float bidPrice = bid.getOrder().getPrice();
			int bidSharesCount = bid.getOrder().getSharesCount();

			WrappedOrder offer = offerIterator.next();
			float offerPrice = offer.getOrder().getPrice();
			int offerSharesCount = offer.getOrder().getSharesCount();

			if (PriceComparator.match(offerPrice, bidPrice)) {
				float dealPrice = mainMatcher.match(offer, bid);
				if (offerSharesCount > bidSharesCount) {
					responses.add(fullyFill(bid, dealPrice));
					responses.add(partiallyFill(offer, dealPrice,
							bidSharesCount));
				} else if (offerSharesCount < bidSharesCount) {
					responses.add(partiallyFill(bid, dealPrice,
							offerSharesCount));
					responses.add(fullyFill(offer, dealPrice));
				} else {
					responses.add(fullyFill(bid, dealPrice));
					responses.add(fullyFill(offer, dealPrice));
				}
			} else
				break;
		}
		return responses;
	}

	private Response partiallyFill(WrappedOrder wrappedOrder, float price,
			int sharesCount) {
		Response response = new Response(wrappedOrder, Status.PARTIALLY_FILLED,
				price, sharesCount);
		wrappedOrder.getOrder().partlyFill(sharesCount);
		log.info("partially filled");
		return response;
	}

	private Response fullyFill(WrappedOrder wrappedOrder, float price) {
		removeOrder(wrappedOrder);
		int sharesCount = wrappedOrder.getOrder().getSharesCount();
		Response response = new Response(wrappedOrder, Status.FULLY_FILLED,
				price, sharesCount);
		log.info("fully filled");
		return response;
	}

}
