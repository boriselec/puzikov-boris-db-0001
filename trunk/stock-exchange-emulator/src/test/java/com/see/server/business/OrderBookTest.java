package com.see.server.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.common.domain.Trade;
import com.see.common.domain.OrderType;
import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;

public class OrderBookTest extends TestCase {

	private OrderBook orderBook = new OrderBookImpl();
	private List<Trade> responses = new LinkedList<>();

	private Order orderGetTest(OrderType type, int sharesCount,
			float price, Date date) {

		Order resultOrder = new Order(UUID.randomUUID(), "TEST", "TEST",
				type, price, sharesCount, new Date());
		return resultOrder;
	}

	@Test
	public void testShouldFullyFillWhenAddedFullyMathingOrders() {

		orderBook.placeOrder(orderGetTest(OrderType.BUY, 2, (float) 1.0,
				new Date()));
		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 0);

		orderBook.placeOrder(orderGetTest(OrderType.SELL, 2, (float) 1.0,
				new Date()));

		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 1);

		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 2);
		assertEquals(numPartiallyFilled, 0);
	}

	private int getPartiallyFilledCoutn(List<Trade> responses2) {
		int count = 0;
		for (Trade response : responses2) {
			if (response.getBid().getQuantity() != response.getOffer()
					.getQuantity())
				count++;
		}
		return count;
	}

	private int getFullyFilledCount(List<Trade> responses2) {
		int count = 0;
		for (Trade response : responses2) {
			count++;
			if (response.getBid().getQuantity() == response.getOffer()
					.getQuantity())
				count++;
		}
		return count;
	}

	@Test
	public void testShouldNotFilledWhenAddedNonMatchingPriceOrder() {
		orderBook.placeOrder(orderGetTest(OrderType.BUY, 2, (float) 1.0,
				new Date()));
		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 0);

		orderBook.placeOrder(orderGetTest(OrderType.SELL, 2, (float) 2.0,
				new Date()));

		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 0);

	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingBid() {
		orderBook.placeOrder(orderGetTest(OrderType.BUY, 2, (float) 1.0,
				new Date()));
		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 0);

		orderBook.placeOrder(orderGetTest(OrderType.SELL, 1, (float) 1.0,
				new Date()));

		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 1);

		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);
	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingOffer() {

		orderBook.placeOrder(orderGetTest(OrderType.BUY, 1, (float) 1.0,
				new Date()));
		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 0);

		orderBook.placeOrder(orderGetTest(OrderType.SELL, 2, (float) 1.0,
				new Date()));

		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 1);

		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);

	}

	@Test
	public void testShouldFillAllOrdersWhenAddedOneToManyMatchingOffer() {
		for (int i = 0; i < 5; i++) {
			orderBook.placeOrder(orderGetTest(OrderType.BUY, 1,
					(float) 1.0, new Date()));
			responses = orderBook.fillOrders();

			assertEquals(responses.size(), 0);
		}

		orderBook.placeOrder(orderGetTest(OrderType.SELL, 5, (float) 1.0,
				new Date()));

		responses = orderBook.fillOrders();

		assertEquals(responses.size(), 5);

	}

	@Test
	public void testShouldCancelWhenReceiveCancelOrder() {
		Order order = orderGetTest(OrderType.BUY, 1, (float) 1.0,
				new Date());
		orderBook.placeOrder(order);
		responses = orderBook.fillOrders();

		try {
			orderBook.cancelOrder(order.getOrderID());
		} catch (CancelOrderException e) {
			fail();
		}

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveBadCancelOrder() {

		Order order = orderGetTest(OrderType.BUY, 1, (float) 1.0,
				new Date());
		orderBook.placeOrder(order);
		responses = orderBook.fillOrders();

		try {
			orderBook.cancelOrder(UUID.randomUUID());
			fail();
		} catch (CancelOrderException e) {
			assertTrue(true);
		}

	}
}
