package com.see.common.utils;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;

import com.see.common.domain.Order;
import com.see.common.domain.OrderType;
import com.see.common.exception.BadOrderException;

public class OrderVerifierTest {

	private final OrderVerifier verifier = new OrderVerifier();

	@Test(expected = BadOrderException.class)
	public void testShouldThrowExceptionWhenStockNotExistInContainer()
			throws BadOrderException {
		String[] tickers = { "t1" };
		Order order = new Order(UUID.randomUUID(), "test", "t2", OrderType.BUY,
				1, 1, new Date());
		verifier.verifyTradeOrder(order, tickers);
	}

	@Test(expected = BadOrderException.class)
	public void testShouldThrowExceptionWhenBadPrice() throws BadOrderException {
		Order order = new Order(UUID.randomUUID(), "test", "t2", OrderType.BUY,
				-1, 1, new Date());
		verifier.verifyTradeOrder(order);
	}

	@Test(expected = BadOrderException.class)
	public void testShouldThrowExceptionWhenBadQuantity()
			throws BadOrderException {
		Order order = new Order(UUID.randomUUID(), "test", "t2", OrderType.BUY,
				1, -1, new Date());
		verifier.verifyTradeOrder(order);
	}
}
