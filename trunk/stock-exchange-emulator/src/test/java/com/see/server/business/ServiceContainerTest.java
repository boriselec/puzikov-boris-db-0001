package com.see.server.business;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.Test;

import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;

public class ServiceContainerTest {

	@Test(expected = IllegalArgumentException.class)
	public void testShouldThrowExceptionWhenPlacedOrderStockNotExist() {
		String[] tickersStrings = { "exist" };
		float[] prices = { (float) 1.0 };
		ServiceContainer container = new ServiceContainer(tickersStrings,
				prices);
		Order order = mock(Order.class);
		when(order.getStock()).thenReturn("notExist");
		container.placeOrder(order);
	}

	@Test(expected = CancelOrderException.class)
	public void testShouldThrowExceptionWhenCancelOrderNotExist()
			throws CancelOrderException {
		String[] tickersStrings = { "exist" };
		float[] prices = { (float) 1.0 };
		ServiceContainer container = new ServiceContainer(tickersStrings,
				prices);
		// all orderbooks are empty
		container.cancelOrder(UUID.randomUUID());
	}
}
