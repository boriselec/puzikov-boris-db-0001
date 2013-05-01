package com.stockexchangeemulator.stockexchange.business;

import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

import com.stockexchangeemulator.domain.InverseOrderComparator;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.Type;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBook {
	private static Logger log = Logger.getLogger(OrderBook.class.getName());

	public OrderBook() {
		bidsOrderBook = new TreeSet<>(new InverseOrderComparator());
		offersOrderBook = new TreeSet<>(new OrderComparator());
		spreadMax = Float.POSITIVE_INFINITY;
		spreadMin = Float.NEGATIVE_INFINITY;
		log.info("OrderBook created");
	}

	TreeSet<WrappedOrder> bidsOrderBook;
	TreeSet<WrappedOrder> offersOrderBook;
	float spreadMax;
	float spreadMin;

	public HashSet<Response> proceedOrder(WrappedOrder wrappedOrder) {

		Order order = wrappedOrder.getOrder();
		Type type = order.getType();

		boolean isInSpread = order.getPrice() > spreadMin
				&& order.getPrice() < spreadMax;
		boolean isProceed = (order.getPrice() <= spreadMin && type == Type.OFFER)
				|| (order.getPrice() <= spreadMax && type == Type.BID);

		switch (type) {

		case BID:
			if (isInSpread == true) {
				bidsOrderBook.add(wrappedOrder);
				spreadMin = order.getPrice();
				log.info("New bid added");
				return null;
			} else if (isProceed == true) {
				return fillOrder(wrappedOrder, Type.BID);

			} else {
				bidsOrderBook.add(wrappedOrder);
				log.info("New bid added");
				return null;
			}

		case OFFER:
			if (isInSpread == true) {
				offersOrderBook.add(wrappedOrder);
				spreadMax = order.getPrice();
				log.info("New offer added");
				return null;
			} else if (isProceed == true) {
				return fillOrder(wrappedOrder, Type.OFFER);

			} else {
				offersOrderBook.add(wrappedOrder);
				log.info("New offer added");
				return null;
			}
		}
		return null;
	}

	private HashSet<Response> fillOrder(WrappedOrder wrappedOrder, Type type) {
		TreeSet<WrappedOrder> inBookOrders;
		TreeSet<WrappedOrder> outBookOrders;
		if (type == Type.BID) {
			inBookOrders = offersOrderBook;
			outBookOrders = bidsOrderBook;
		} else {
			inBookOrders = bidsOrderBook;
			outBookOrders = offersOrderBook;

		}
		Order order = wrappedOrder.getOrder();
		HashSet<Response> responses = new HashSet<>();
		Iterator<WrappedOrder> iterator = inBookOrders.iterator();

		boolean isFullyFilled = false;
		while (iterator.hasNext()) {
			WrappedOrder nextOrder = iterator.next();
			boolean isNoMoreOrders;
			if (type == Type.BID)
				isNoMoreOrders = nextOrder.getOrder().getPrice() > order
						.getPrice();
			else
				isNoMoreOrders = nextOrder.getOrder().getPrice() < order
						.getPrice();
			if (isNoMoreOrders == true) {
				outBookOrders.add(wrappedOrder);
				break;
			}
			if (order.getSharesCount() > nextOrder.getOrder().getSharesCount()) {
				responses.add(new Response(nextOrder, Status.FULLY_FILLED));
				inBookOrders.remove(nextOrder);
				wrappedOrder.getOrder().partlyFill(
						nextOrder.getOrder().getSharesCount());
				log.info("Fully filled");
			} else if (order.getSharesCount() == nextOrder.getOrder()
					.getSharesCount()) {
				responses.add(new Response(nextOrder, Status.FULLY_FILLED));
				responses.add(new Response(wrappedOrder, Status.FULLY_FILLED));
				inBookOrders.remove(nextOrder);
				isFullyFilled = true;
				log.info("Fully filled");
				log.info("Fully filled");
				break;
			} else {
				responses.add(new Response(wrappedOrder, Status.FULLY_FILLED));
				responses.add(new Response(nextOrder, Status.PARTIALLY_FILLED));
				nextOrder.getOrder().partlyFill(order.getSharesCount());
				isFullyFilled = true;
				log.info("Fully filled");
				log.info("Partly filled");
			}
		}
		if (isFullyFilled == false) {
			responses.add(new Response(wrappedOrder, Status.PARTIALLY_FILLED));
			outBookOrders.add(wrappedOrder);
			log.info("Partly filled");

		}
		return responses;
	}

}
