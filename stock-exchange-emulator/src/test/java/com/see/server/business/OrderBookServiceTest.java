package com.see.server.business;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.common.domain.Order;
import com.see.common.domain.Trade;
import com.see.server.TradeListener;

public class OrderBookServiceTest extends TestCase {
	@Test
	public void testShouldNotifyListenersWhenTradeDone() {

		TradeListener listener = mock(TradeListener.class);
		when(listener.getClientName()).thenReturn("test");

		OrderBook orderBook = mock(OrderBook.class);
		Trade trade = mock(Trade.class);

		Order order = mock(Order.class);
		when(order.getOrderID()).thenReturn(UUID.randomUUID());
		when(order.getClientName()).thenReturn("test");
		when(trade.getBid()).thenReturn(order);
		when(trade.getOffer()).thenReturn(order);

		List<Trade> result = new ArrayList<>();
		result.add(trade);
		when(orderBook.fillOrders()).thenReturn(result);

		OrderBookService service = new OrderBookService(orderBook);
		service.addListener(listener);

		service.placeOrder(order);

		verify(listener, atLeastOnce()).onTrade(trade);
	}
}
