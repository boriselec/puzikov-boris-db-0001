package com.see.server.business;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.CancelResponse;
import com.see.common.domain.ErrorResponse;
import com.see.common.domain.Order;
import com.see.common.domain.OrderBookResponse;
import com.see.common.domain.Trade;
import com.see.common.domain.TradeOperation;
import com.see.common.domain.TradeOrder;
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
		assertEquals(responses.getFirst() instanceof Trade, true);
		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 2);
		assertEquals(numPartiallyFilled, 0);
	}

	private int getPartiallyFilledCoutn(LinkedList<OrderBookResponse> responses2) {
		int count = 0;
		for (OrderBookResponse response : responses2) {
			if (((Trade) response).getBid().getSharesCount() > 0)
				count++;
			if (((Trade) response).getOffer().getSharesCount() > 0)
				count++;
		}
		return count;
	}

	private int getFullyFilledCount(LinkedList<OrderBookResponse> responses2) {
		int count = 0;
		for (OrderBookResponse response : responses2) {
			if (((Trade) response).getBid().getSharesCount() == 0)
				count++;
			if (((Trade) response).getOffer().getSharesCount() == 0)
				count++;
		}
		return count;
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
		assertEquals(responses.getFirst() instanceof Trade, true);

		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
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
		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst() instanceof Trade, true);

		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);

	}

	@Test
	public void testShouldFillAllOrdersWhenAddedOneToManyMatchingOffer() {
		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.OFFER,
				2, (float) 1.0, new Date()));
		assertEquals(responses.size(), 0);
		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 1,
				Float.POSITIVE_INFINITY, new Date()));
		assertEquals(responses.size(), 1);
		int numFullyFilled = getFullyFilledCount(responses);
		int numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 1);
		assertEquals(numPartiallyFilled, 1);
		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 1,
				Float.POSITIVE_INFINITY, new Date()));
		assertEquals(responses.size(), 1);
		numFullyFilled = getFullyFilledCount(responses);
		numPartiallyFilled = getPartiallyFilledCoutn(responses);
		assertEquals(numFullyFilled, 2);
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
		assertEquals(responses.getFirst() instanceof CancelResponse, true);

	}

	@Test
	public void testShouldReturnErrorResponseWhenReceiveBadCancelOrder() {

		responses = orderBook.proceedOrder(orderGetTest(TradeOperation.BID, 2,
				(float) 1.0, new Date()));

		UUID nonExistent = UUID.randomUUID();
		responses = orderBook.proceedOrder(orderGetCancelTest(nonExistent));

		assertEquals(responses.size(), 1);
		assertEquals(responses.getFirst() instanceof ErrorResponse, true);

	}
}
