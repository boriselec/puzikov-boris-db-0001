package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import com.stockexchangeemulator.domain.ConstantPriceMatcher;
import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.LastPriceMatcher;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.PriceComparator;
import com.stockexchangeemulator.domain.PriceMatcher;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBook {
	private static float DEFAULT_PRICE = (float) 100.0;

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
	private float lastPrice = DEFAULT_PRICE;

	public LinkedList<Response> proceedOrder(WrappedOrder wrappedOrder) {
		if (wrappedOrder.getOrder().getType() == Operation.CANCEL) {
			return removeOrder(wrappedOrder);
		} else {
			addOrder(wrappedOrder);
			return fillOrders();
		}
	}

	private LinkedList<Response> removeOrder(WrappedOrder wrappedOrder) {
		LinkedList<Response> response = new LinkedList<>();
		int opderID = wrappedOrder.getOrder().getOrderID();

		for (WrappedOrder order : bidsOrderBook) {
			if (order.getOrderID() == opderID) {
				bidsOrderBook.remove(order);
				response.add(new Response(wrappedOrder, Status.CANCELED,
						"Order canceled", 0, 0, null));
				return response;
			}
		}
		for (WrappedOrder order : offersOrderBook) {
			if (order.getOrderID() == opderID) {
				offersOrderBook.remove(order);
				response.add(new Response(wrappedOrder, Status.CANCELED,
						"Order canceled", 0, 0, null));
				return response;
			}
		}
		response.add(new Response(wrappedOrder, Status.ERROR, "Can't cancel",
				0, 0, null));
		return response;
	}

	private void addOrder(WrappedOrder wrappedOrder) {
		switch (wrappedOrder.getOrder().getType()) {

		case BID:
			bidsOrderBook.add(wrappedOrder);
			break;

		case OFFER:
			offersOrderBook.add(wrappedOrder);
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private void removeFirst(TreeSet<WrappedOrder> book) {
		Iterator<WrappedOrder> iterator = book.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
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

			WrappedOrder offer = offerIterator.next();
			float offerPrice = offer.getOrder().getPrice();

			if (PriceComparator.match(offerPrice, bidPrice)) {
				float dealPrice = mainMatcher.match(offer, bid, lastPrice);
				lastPrice = dealPrice;

				LinkedList<Response> dealResponses = fill(bid, offer, dealPrice);
				spliceResponcesWithSameOrderID(responses, dealResponses);
			} else
				break;
		}
		return responses;
	}

	private LinkedList<Response> fill(WrappedOrder order1, WrappedOrder order2,
			float dealPrice) {
		LinkedList<Response> responses = new LinkedList<>();
		int order1SharesCount = order1.getOrder().getSharesCount();
		int order2SharesCount = order2.getOrder().getSharesCount();

		if (order1SharesCount == order2SharesCount) {
			responses.add(fullyFill(order1, dealPrice));
			responses.add(fullyFill(order2, dealPrice));
			return responses;
		} else if (order1SharesCount > order2SharesCount) {
			responses.add(partiallyFill(order1, dealPrice, order2SharesCount));
			responses.add(fullyFill(order2, dealPrice));
			return responses;

		} else if (order1SharesCount < order2SharesCount) {
			responses.add(fullyFill(order1, dealPrice));
			responses.add(partiallyFill(order2, dealPrice, order1SharesCount));
			return responses;

		}
		return null;
	}

	private Response partiallyFill(WrappedOrder wrappedOrder, float price,
			int sharesCount) {
		Date dealDate = new Date();
		Response response = new Response(wrappedOrder, Status.PARTIALLY_FILLED,
				"Ok", price, sharesCount, dealDate);
		wrappedOrder.getOrder().partliallyFill(sharesCount);
		return response;
	}

	private Response fullyFill(WrappedOrder wrappedOrder, float price) {
		Date dealDate = new Date();
		int sharesCount = wrappedOrder.getOrder().getSharesCount();
		Response response = new Response(wrappedOrder, Status.FULLY_FILLED,
				"Ok", price, sharesCount, dealDate);

		if (wrappedOrder.getOrder().getType() == Operation.BID)
			removeFirst(bidsOrderBook);
		else
			removeFirst(offersOrderBook);
		return response;
	}

	private void spliceResponcesWithSameOrderID(LinkedList<Response> responses,
			LinkedList<Response> dealResponses) {
		l: for (Response response : dealResponses) {
			for (Response oldResponse : responses) {
				if (oldResponse.getOrderID() == response.getOrderID()) {
					oldResponse.splice(response);
					continue l;
				}
			}
			responses.add(response);
		}
	}
}
