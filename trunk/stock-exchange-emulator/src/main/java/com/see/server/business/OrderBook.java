package com.see.server.business;

import java.util.List;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.domain.Trade;
import com.see.common.exception.CancelOrderException;

public interface OrderBook {

	public void placeOrder(Order order);

	public void cancelOrder(UUID orderID) throws CancelOrderException;

	public List<Trade> fillOrders();

}