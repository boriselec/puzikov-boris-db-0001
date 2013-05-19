package com.stockexchangeemulator.stockexchange.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.domain.CancelOrder;
import com.see.domain.CancelResponse;
import com.see.domain.ErrorResponse;
import com.see.domain.Order;
import com.see.domain.OrderBookResponse;
import com.see.domain.TradeOperation;
import com.see.domain.TradeOrder;
import com.see.server.business.OrderBook;

public class OrderBookTest extends TestCase {

	private OrderBook orderBook = new OrderBook();
	private LinkedList<OrderBookResponse> responses;

	private Order orderGetTest(TradeOperation type, int sharesCount,
			float price, Date date) {

		Order resultOrder = new TradeOrder("Test", "TEST", type, sharesCount,
				price);
		resultOrder.setDate(new Date());
		return resultOrder;
	}

	private Order orderGetCancelTest(UUID orderID) {

		Order resultOrder = new CancelOrder("TEST", "TEST", orderID);
		resultOrder.setDate(new Date());
		return resultOrder;
	}

	@Test
	public void testShouldFullyFillWhenAddedFullyMathingOrders() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));

		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				2, (float) 1.0, new Date()));

		assertEquals(responses.size(), 1);
		// int numFullyFilled = 0;
		// int numPartiallyFilled = 0;
		// for (OrderBookResponse response : responses) {
		// if (response.getStatus() == Status.FULLY_FILLED)
		// numFullyFilled++;
		// else if (response.getStatus() == Status.PARTIALLY_FILLED)
		// numPartiallyFilled++;
		// }
		// assertEquals(numFullyFilled, 2);
		// assertEquals(numPartiallyFilled, 0);
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
		assertEquals(responses.size(), 1);

		// int numFullyFilled = 0;
		// int numPartiallyFilled = 0;
		// for (OrderBookResponse response : responses) {
		// if (response.getStatus() == Status.FULLY_FILLED)
		// numFullyFilled++;
		// else if (response.getStatus() == Status.PARTIALLY_FILLED)
		// numPartiallyFilled++;
		// }
		// assertEquals(numFullyFilled, 1);
		// assertEquals(numPartiallyFilled, 1);
	}

	@Test
	public void testShouldPartiallyFillWhenAddedPartiallyMathingOffer() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				1, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));
		assertEquals(responses.size(), 1);

		// int numFullyFilled = 0;
		// int numPartiallyFilled = 0;
		// for (OrderBookResponse response : responses) {
		// if (response.getStatus() == Status.FULLY_FILLED)
		// numFullyFilled++;
		// else if (response.getStatus() == Status.PARTIALLY_FILLED)
		// numPartiallyFilled++;
		// }
		// assertEquals(numFullyFilled, 1);
		// assertEquals(numPartiallyFilled, 1);
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
		assertEquals(responses.size(), 5);

		// int numFullyFilled = 0;
		// int numPartiallyFilled = 0;
		// for (OrderBookResponse response : responses) {
		// if (response.getStatus() == Status.FULLY_FILLED)
		// numFullyFilled++;
		// else if (response.getStatus() == Status.PARTIALLY_FILLED)
		// numPartiallyFilled++;
		// }
		// assertEquals(numFullyFilled, 6);
		// assertEquals(numPartiallyFilled, 0);

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
		assertEquals(responses.getFirst() instanceof CancelResponse, true);

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveCancelOrderWithNoSuchOrderInBook() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));

		UUID nonExistent = UUID.randomUUID();
		responses = orderBook.proceedOrder(orderGetCancelTest(nonExistent));

		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst() instanceof ErrorResponse, true);

	}
}
