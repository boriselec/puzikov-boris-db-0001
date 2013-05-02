package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;

import junit.framework.TestCase;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderComparator;
import com.stockexchangeemulator.domain.Type;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBookTest extends TestCase {

	public void test() {
		OrderBook orderBook = new OrderBook();
		Order order = new Order("IBM", Type.OFFER, 2, (float) 1.0);
		Order order2 = new Order("IBM", Type.BID, 1, (float) 1.0);
		Order order3 = new Order("IBM", Type.BID, 1, (float) 1.0);
		Date date = new Date(2002, 1, 1, 1, 1, 1);
		Date date2 = new Date(2003, 1, 1, 1, 1, 1);
		WrappedOrder wrappedOrder = new WrappedOrder(0, 0, order, date);
		WrappedOrder wrappedOrder3 = new WrappedOrder(0, 4, order3, date);
		WrappedOrder wrappedOrder2 = new WrappedOrder(0, 3, order2, date);
		OrderComparator comparator = new OrderComparator();
		orderBook.proceedOrder(wrappedOrder2);
		orderBook.proceedOrder(wrappedOrder3);
		orderBook.proceedOrder(wrappedOrder);
	}
}
