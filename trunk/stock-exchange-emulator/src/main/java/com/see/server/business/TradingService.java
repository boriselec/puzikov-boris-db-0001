package com.see.server.business;

import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.CancelOrderException;
import com.see.server.TradeListener;

public interface TradingService {

	public void placeOrder(Order order);

	public void cancelOrder(UUID orderID) throws CancelOrderException;

	public void addListener(TradeListener listener);

	public void removeListener(TradeListener listener);

}
