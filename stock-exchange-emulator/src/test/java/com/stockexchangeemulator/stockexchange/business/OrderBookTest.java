package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.LinkedList;

import junit.framework.TestCase;

import org.junit.Test;

import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;

public class OrderBookTest extends TestCase {

	private OrderBook orderBook = new OrderBook();
	private LinkedList<Response> responses;
	private int idCount = 0;

	private Order orderGetTest(Operation type, int sharesCount, float price,
			Date date) {

		Order resultOrder = new Order("Test", "TEST", type, sharesCount, price);
		resultOrder.setDate(new Date());
		resultOrder.setOrderID(idCount++);
		return resultOrder;
	}

	private Order orderGetCancelTest(int orderID) {

		Order resultOrder = new Order("TEST", Operation.CANCEL, orderID);
		resultOrder.setDate(new Date());
		resultOrder.setOrderID(idCount++);
		resultOrder.setPreviousOrderID(orderID);
		return resultOrder;
	}

	@Test
	public void testShouldFullyFillWhenAddedFullyMathingOrders() {

		responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 2,
				(float) 1.0, new Date()));

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(Operation.OFFER, 2,
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
		assertEquals(numFullyFilled, 2);
		assertEquals(numPartiallyFilled, 0);
	}

	@Test
	public void testShouldNotFilledWhenAddedNonMatchingPriceOrder() {
		responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 2,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(Operation.OFFER, 2,
				(float) 2.0, new Date()));
		assertEquals(responses.size(), 0);

	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingBid() {

		responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 1,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(Operation.OFFER, 2,
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
	public void testShouldPartiallyFillWhenAddedPartiallyMathingOffer() {

		responses = orderBook.proceedOrder(orderGetTest(Operation.OFFER, 1,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 2,
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
			responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 1,
					Float.POSITIVE_INFINITY, new Date()));
			assertEquals(responses.size(), 0);
		}

		responses = orderBook.proceedOrder(orderGetTest(Operation.OFFER, 5,
				(float) 1.0, new Date()));
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
		Order order = orderGetTest(Operation.BID, 2, (float) 1.0, new Date());
		responses = orderBook.proceedOrder(order);

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetCancelTest(order
				.getOrderID()));
		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.CANCELED);

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveCancelOrderWithNoSuchOrderInBook() {

		responses = orderBook.proceedOrder(orderGetTest(Operation.BID, 2,
				(float) 1.0, new Date()));

		int nonExistent = 5;
		responses = orderBook.proceedOrder(orderGetCancelTest(nonExistent));

		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst().getStatus(), Status.ERROR);

	}
}
