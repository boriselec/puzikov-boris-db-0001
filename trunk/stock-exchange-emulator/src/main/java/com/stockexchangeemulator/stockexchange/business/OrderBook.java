package com.stockexchangeemulator.stockexchange.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import com.stockexchangeemulator.domain.CancelOrder;
import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.LastPriceMatcher;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.PriceComparator;
import com.stockexchangeemulator.domain.PriceMatcher;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.ResponseManager;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.TradeOperation;
import com.stockexchangeemulator.domain.TradeOrder;

public class OrderBook {
	private static float DEFAULT_PRICE = (float) 100.0;

	public OrderBook() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());

		// this.startupMatcher = new ConstantPriceMatcher(getAveragePrice());
		this.mainMatcher = new LastPriceMatcher();
		this.responseManager = new ResponseManager();
	}

	// private float getAveragePrice() {
	// // TODO: return average price for all crossing orders
	// return (float) 0.0;
	// }

	private TreeSet<TradeOrder> bidsOrderBook;
	private TreeSet<TradeOrder> offersOrderBook;

	private PriceMatcher mainMatcher;
	// private PriceMatcher startupMatcher;
	private float lastDealPrice = DEFAULT_PRICE;

	private ResponseManager responseManager;

	public LinkedList<Response> proceedOrder(Order order) {
		if (order instanceof CancelOrder) {
			return removeOrder((CancelOrder) order);
		} else {
			addOrder((TradeOrder) order);
			return fillOrders();
		}
	}

	private LinkedList<Response> removeOrder(CancelOrder cancelOrder) {
		LinkedList<Response> response = new LinkedList<>();
		int opderID = cancelOrder.getCancelingOrderID();

		for (TradeOrder order : bidsOrderBook) {
			if (order.getCancelingOrderID() == opderID) {
				bidsOrderBook.remove(order);
				response.add(responseManager.createCancelingResponse(order));
				return response;
			}
		}
		for (TradeOrder order : offersOrderBook) {
			if (order.getCancelingOrderID() == opderID) {
				offersOrderBook.remove(order);
				response.add(responseManager.createCancelingResponse(order));
				return response;
			}
		}

		// not found
		response.add(responseManager.createErrorResponse(cancelOrder,
				Status.ERROR));
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

	private LinkedList<Response> fillOrders() {
		LinkedList<Response> responses = new LinkedList<>();

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

				LinkedList<Response> dealResponses = fill(bid, offer, dealPrice);
				responseManager.spliceResponcesWithSameOrderID(responses,
						dealResponses);
			} else
				break;
		}
		return responses;
	}

	private LinkedList<Response> fill(TradeOrder order1, TradeOrder order2,
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

		} else { // if (order1SharesCount < order2SharesCount)
			responses.add(fullyFill(order1, dealPrice));
			responses.add(partiallyFill(order2, dealPrice, order1SharesCount));
			return responses;

		}
	}

	private Response partiallyFill(TradeOrder order, float price,
			int sharesCount) {
		order.partliallyFill(price, sharesCount);
		Response response = responseManager.createFilledResponse(order,
				Status.PARTIALLY_FILLED);
		return response;
	}

	private Response fullyFill(TradeOrder order, float price) {
		order.fullyFill(price);
		Response response = responseManager.createFilledResponse(order,
				Status.FULLY_FILLED);

		removeFirst(order.getType());
		return response;
	}

}
