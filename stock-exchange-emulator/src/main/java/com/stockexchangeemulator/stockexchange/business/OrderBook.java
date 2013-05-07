package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import com.stockexchangeemulator.domain.ConstantPriceMatcher;
import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.LastPriceMatcher;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.PriceComparator;
import com.stockexchangeemulator.domain.PriceMatcher;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;

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

	private TreeSet<Order> bidsOrderBook;
	private TreeSet<Order> offersOrderBook;

	private PriceMatcher mainMatcher;
	private PriceMatcher startupMatcher;
	private float lastPrice = DEFAULT_PRICE;

	public LinkedList<Response> proceedOrder(Order order) {
		if (order.getType() == Operation.CANCEL) {
			return removeOrder(order);
		} else {
			addOrder(order);
			return fillOrders();
		}
	}

	private LinkedList<Response> removeOrder(Order cancelOrder) {
		LinkedList<Response> response = new LinkedList<>();
		int opderID = cancelOrder.getpreviousOrderID();

		for (Order order : bidsOrderBook) {
			if (order.getOrderID() == opderID) {
				bidsOrderBook.remove(order);
				response.add(new Response(order, Status.CANCELED,
						"Order canceled", 0, 0, null));
				return response;
			}
		}
		for (Order order : offersOrderBook) {
			if (order.getOrderID() == opderID) {
				offersOrderBook.remove(order);
				response.add(new Response(order, Status.CANCELED,
						"Order canceled", 0, 0, null));
				return response;
			}
		}
		response.add(new Response(cancelOrder, Status.ERROR, "Can't cancel", 0,
				0, null));
		return response;
	}

	private void addOrder(Order order) {
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

	private void removeFirst(TreeSet<Order> book) {
		Iterator<Order> iterator = book.iterator();
		if (iterator.hasNext()) {
			iterator.next();
			iterator.remove();
		}
	}

	private LinkedList<Response> fillOrders() {
		LinkedList<Response> responses = new LinkedList<>();

		while (true) {
			Iterator<Order> bidIterator = bidsOrderBook.iterator();
			Iterator<Order> offerIterator = offersOrderBook.iterator();
			if (bidsOrderBook.size() == 0 || offersOrderBook.size() == 0)
				break;

			Order bid = bidIterator.next();
			float bidPrice = bid.getPrice();

			Order offer = offerIterator.next();
			float offerPrice = offer.getPrice();

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

	private LinkedList<Response> fill(Order order1, Order order2,
			float dealPrice) {
		LinkedList<Response> responses = new LinkedList<>();
		int order1SharesCount = order1.getSharesCount();
		int order2SharesCount = order2.getSharesCount();

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

	private Response partiallyFill(Order order, float price, int sharesCount) {
		Date dealDate = new Date();
		Response response = new Response(order, Status.PARTIALLY_FILLED, "Ok",
				price, sharesCount, dealDate);
		order.partliallyFill(sharesCount);
		return response;
	}

	private Response fullyFill(Order order, float price) {
		Date dealDate = new Date();
		int sharesCount = order.getSharesCount();
		Response response = new Response(order, Status.FULLY_FILLED, "Ok",
				price, sharesCount, dealDate);

		if (order.getType() == Operation.BID)
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
