package com.stockexchangeemulator.stockexchange.business;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.PriceComparator;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBook {
	private static Logger log = Logger.getLogger(OrderBook.class.getName());

	public OrderBook() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());
		spreadMax = Float.POSITIVE_INFINITY;
		spreadMin = Float.NEGATIVE_INFINITY;
	}

	TreeSet<WrappedOrder> bidsOrderBook;
	TreeSet<WrappedOrder> offersOrderBook;
	float spreadMax;
	float spreadMin;

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
				if (offerSharesCount > bidSharesCount) {
					responses.add(fullyFill(bid));
					responses.add(partiallyFill(offer, bidSharesCount));
				} else if (offerSharesCount < bidSharesCount) {
					responses.add(partiallyFill(bid, offerSharesCount));
					responses.add(fullyFill(offer));
				} else {
					responses.add(fullyFill(bid));
					responses.add(fullyFill(offer));
				}
			} else
				break;
		}
		return responses;
	}

	private Response partiallyFill(WrappedOrder wrappedOrder, int sharesCount) {
		Response response = new Response(wrappedOrder, Status.PARTIALLY_FILLED,
				sharesCount);
		wrappedOrder.getOrder().partlyFill(sharesCount);
		log.info("partially filled");
		return response;
	}

	private Response fullyFill(WrappedOrder wrappedOrder) {
		removeOrder(wrappedOrder);
		int sharesCount = wrappedOrder.getOrder().getSharesCount();
		Response response = new Response(wrappedOrder, Status.FULLY_FILLED,
				sharesCount);
		log.info("fully filled");
		return response;
	}

	// public LinkedList<Response> proceedOrderOld(WrappedOrder wrappedOrder) {
	//
	// Order order = wrappedOrder.getOrder();
	// Type type = order.getType();
	//
	// boolean isInSpread = order.getPrice() > spreadMin
	// && order.getPrice() < spreadMax;
	// boolean isProceed = (order.getPrice() <= spreadMin && type == Type.OFFER)
	// || (order.getPrice() <= spreadMax && type == Type.BID);
	//
	// switch (type) {
	//
	// case BID:
	// if (isInSpread == true) {
	// bidsOrderBook.add(wrappedOrder);
	// spreadMin = order.getPrice();
	// log.info("New bid added");
	// return null;
	// } else if (isProceed == true) {
	// return fillOrderOld(wrappedOrder, Type.BID);
	//
	// } else {
	// bidsOrderBook.add(wrappedOrder);
	// log.info("New bid added");
	// return null;
	// }
	//
	// case OFFER:
	// if (isInSpread == true) {
	// offersOrderBook.add(wrappedOrder);
	// spreadMax = order.getPrice();
	// log.info("New offer added");
	// return null;
	// } else if (isProceed == true) {
	// return fillOrderOld(wrappedOrder, Type.OFFER);
	//
	// } else {
	// offersOrderBook.add(wrappedOrder);
	// log.info("New offer added");
	// return null;
	// }
	// }
	// return null;
	// }
	//
	// private LinkedList<Response> fillOrderOld(WrappedOrder wrappedOrder,
	// Type type) {
	//
	// TreeSet<WrappedOrder> proposalBook = (type == Type.BID) ? offersOrderBook
	// : bidsOrderBook;
	// TreeSet<WrappedOrder> orderBook = (type == Type.BID) ? bidsOrderBook
	// : offersOrderBook;
	//
	// float orderPrice = wrappedOrder.getOrder().getPrice();
	// int orderSharesCount = wrappedOrder.getOrder().getSharesCount();
	//
	// LinkedList<Response> responses = new LinkedList<>();
	// Iterator<WrappedOrder> iterator = proposalBook.iterator();
	//
	// boolean isFullyFilled = false;
	// while (iterator.hasNext()) {
	// WrappedOrder proposalOrder = iterator.next();
	// float proposalPrice = proposalOrder.getOrder().getPrice();
	// int proposalSharesCount = proposalOrder.getOrder().getSharesCount();
	//
	// boolean isNoMoreOrders = (proposalPrice > orderPrice && type == Type.BID)
	// || (proposalPrice < orderPrice && type == Type.OFFER);
	//
	// if (isNoMoreOrders == true) {
	// break;
	// }
	//
	// if (orderSharesCount > proposalSharesCount) {
	// responses.add(new Response(proposalOrder, Status.FULLY_FILLED));
	// proposalBook.remove(proposalOrder);
	// wrappedOrder.getOrder().partlyFill(proposalSharesCount);
	// log.info("Partly filled - Fully filled");
	//
	// } else if (orderSharesCount == proposalSharesCount) {
	// responses.add(new Response(proposalOrder, Status.FULLY_FILLED));
	// responses.add(new Response(wrappedOrder, Status.FULLY_FILLED));
	// proposalBook.remove(proposalOrder);
	// isFullyFilled = true;
	// log.info("Fully filled - Fully filled");
	// break;
	//
	// } else {
	// responses.add(new Response(wrappedOrder, Status.FULLY_FILLED));
	// responses.add(new Response(proposalOrder,
	// Status.PARTIALLY_FILLED));
	// proposalOrder.getOrder().partlyFill(orderSharesCount);
	// isFullyFilled = true;
	// log.info("Fully filled - Partly filled");
	// break;
	// }
	// }
	// if (isFullyFilled == false) {
	// responses.add(new Response(wrappedOrder, Status.PARTIALLY_FILLED));
	// orderBook.add(wrappedOrder);
	// log.info("New offer/bid added");
	// }
	//
	// return responses;
	// }
}
