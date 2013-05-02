package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
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

	private LinkedList<Response> fillOrders() {
		LinkedList<Response> responses = new LinkedList<>();

		while (true) {
			Iterator<WrappedOrder> bidIterator = bidsOrderBook.iterator();
			Iterator<WrappedOrder> offerIterator = offersOrderBook.iterator();
			if (bidsOrderBook.size() == 0 || offersOrderBook.size() == 0)
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
					Response response = fullyFill(bid, dealPrice);
					spliceResponce(responses, response);
					bidIterator.remove();
					response = partiallyFill(offer, dealPrice, bidSharesCount);
					spliceResponce(responses, response);
				} else if (offerSharesCount < bidSharesCount) {
					Response response = partiallyFill(bid, dealPrice,
							offerSharesCount);
					spliceResponce(responses, response);
					response = fullyFill(offer, dealPrice);
					spliceResponce(responses, response);
					offerIterator.remove();
				} else {
					Response response = fullyFill(bid, dealPrice);
					spliceResponce(responses, response);
					bidIterator.remove();
					response = fullyFill(offer, dealPrice);
					spliceResponce(responses, response);
					offerIterator.remove();
				}
			} else
				break;
		}
		return responses;
	}

	private void spliceResponce(LinkedList<Response> responses,
			Response response) {
		for (Response oldResponse : responses) {
			if (oldResponse.getOrderID() == response.getOrderID()) {
				oldResponse.splice(response);
				return;
			}
		}
		responses.add(response);
	}

	private Response partiallyFill(WrappedOrder wrappedOrder, float price,
			int sharesCount) {
		Date dealDate = new Date();
		Response response = new Response(wrappedOrder, Status.PARTIALLY_FILLED,
				price, sharesCount, dealDate);
		wrappedOrder.getOrder().partlyFill(sharesCount);
		log.info("partially filled" + wrappedOrder.getOrderID());
		return response;
	}

	private Response fullyFill(WrappedOrder wrappedOrder, float price) {
		Date dealDate = new Date();
		int sharesCount = wrappedOrder.getOrder().getSharesCount();
		Response response = new Response(wrappedOrder, Status.FULLY_FILLED,
				price, sharesCount, dealDate);
		log.info("fully filled" + wrappedOrder.getOrderID());
		return response;
	}

}
