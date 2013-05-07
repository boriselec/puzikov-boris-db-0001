package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;

import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class OrderBookTest extends TestCase {

	private OrderBook orderBook = new OrderBook();
	private LinkedList<Response> responses;
	private int idCount = 0;

	private WrappedOrder wrappedOrderGetTest(Operation type, int sharesCount,
			float price, Date date) {

		return new WrappedOrder("TEST", idCount++, new Order("Test", "TEST",
				type, sharesCount, price), date);
	}

	private WrappedOrder wrappedOrderGetCancelTest(int orderID) {

		return new WrappedOrder("TEST", idCount++, new Order("TEST",
				Operation.CANCEL, orderID), new Date());
	}

	@Test
	public void testShouldFullyFillWhenAddedFullyMathingOrders() {

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
				2, (float) 1.0, new Date()));

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.OFFER,
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
		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
				2, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.OFFER,
				2, (float) 2.0, new Date()));
		assertEquals(responses.size(), 0);

	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingBid() {

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
				1, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.OFFER,
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

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.OFFER,
				1, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
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
	public void testShouldFillAllOrdersWhenAddedOneToManyMatchingOffer() {
		for (int i = 0; i < 5; i++) {
			responses = orderBook.proceedOrder(wrappedOrderGetTest(
					Operation.BID, 1, Float.POSITIVE_INFINITY, new Date()));
			assertEquals(responses.size(), 0);
		}

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.OFFER,
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

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
				2, (float) 1.0, new Date()));

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(wrappedOrderGetCancelTest(0));
		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.CANCELED);

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveCancelOrderWithNoSuchOrderInBook() {

		responses = orderBook.proceedOrder(wrappedOrderGetTest(Operation.BID,
				2, (float) 1.0, new Date()));

		int nonExistent = 5;
		responses = orderBook
				.proceedOrder(wrappedOrderGetCancelTest(nonExistent));

		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.ERROR);

	}
}
