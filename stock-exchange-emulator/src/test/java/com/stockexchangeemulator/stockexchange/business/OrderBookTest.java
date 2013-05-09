package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;

import com.stockexchangeemulator.domain.CancelOrder;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.TradeOperation;
import com.stockexchangeemulator.domain.TradeOrder;

public class OrderBookTest extends TestCase {

	private OrderBook orderBook = new OrderBook();
	private LinkedList<Response> responses;
	private int idCount = 0;

	private Order orderGetTest(TradeOperation type, int sharesCount,
			float price, Date date) {

		Order resultOrder = new TradeOrder("Test", "TEST", type, sharesCount,
				price);
		resultOrder.setDate(new Date());
		resultOrder.setOrderID(idCount++);
		return resultOrder;
	}

	private Order orderGetCancelTest(int orderID) {

		Order resultOrder = new CancelOrder("TEST", "TEST", orderID);
		resultOrder.setDate(new Date());
		resultOrder.setOrderID(idCount++);
		return resultOrder;
	}

	@Test
	public void testShouldFullyFillWhenAddedFullyMathingOrders() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				2, (float) 1.0, new Date()));
		assertEquals(responses.size(), 2);

		int numFullyFilled = 0;
		int numPartiallyFilled = 0;
		for (Response response : responses) {
			if (response.getStatus() == Status.FULLY_FILLED)
				numFullyFilled++;
			else if (response.getStatus() == Status.PARTIALLY_FILLED)
				numPartiallyFilled++;
		}
		assertEquals(numFullyFilled, 2);
		assertEquals(numPartiallyFilled, 0);
	}

	@Test
	public void testShouldNotFilledWhenAddedNonMatchingPriceOrder() {
		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				2, (float) 2.0, new Date()));
		assertEquals(responses.size(), 0);

	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingBid() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 1,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				2, (float) 1.0, new Date()));
		assertEquals(responses.size(), 2);

		int numFullyFilled = 0;
		int numPartiallyFilled = 0;
		for (Response response : responses) {
			if (response.getStatus() == Status.FULLY_FILLED)
				numFullyFilled++;
			else if (response.getStatus() == Status.PARTIALLY_FILLED)
				numPartiallyFilled++;
		}
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);
	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingOffer() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				1, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 2);

		int numFullyFilled = 0;
		int numPartiallyFilled = 0;
		for (Response response : responses) {
			if (response.getStatus() == Status.FULLY_FILLED)
				numFullyFilled++;
			else if (response.getStatus() == Status.PARTIALLY_FILLED)
				numPartiallyFilled++;
		}
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);
	}

	@Test
	public void testShouldFillAllOrdersWhenAddedOneToManyMatchingOffer() {
		for (int i = 0; i < 5; i++) {
			responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID,
					1, Float.POSITIVE_INFINITY, new Date()));
			assertEquals(responses.size(), 0);
		}

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				5, (float) 1.0, new Date()));
		assertEquals(responses.size(), 6);

		int numFullyFilled = 0;
		int numPartiallyFilled = 0;
		for (Response response : responses) {
			if (response.getStatus() == Status.FULLY_FILLED)
				numFullyFilled++;
			else if (response.getStatus() == Status.PARTIALLY_FILLED)
				numPartiallyFilled++;
		}
		assertEquals(numFullyFilled, 6);
		assertEquals(numPartiallyFilled, 0);

	}

	@Test
	public void testShouldCancelWhenReceiveCancelOrder() {
		Order order = orderGetTest(TradeOperation.BID, 2, (float) 1.0,
				new Date());
		responses = orderBook.proceedOrder(order);

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetCancelTest(order
				.getCancelingOrderID()));
		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.CANCELED);

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveCancelOrderWithNoSuchOrderInBook() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));

		int nonExistent = 5;
		responses = orderBook.proceedOrder(orderGetCancelTest(nonExistent));

		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.ERROR);

	}
}
