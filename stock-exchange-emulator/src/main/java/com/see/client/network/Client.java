package com.see.client.network;

import java.util.UUID;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.NoLoginException;
import com.see.common.message.OrderMessage;

public interface Client {
	public void login(String loginName) throws NoLoginException;

	public UUID sendOrder(OrderMessage order) throws BadOrderException,
			NoLoginException;

	public void cancelOrder(UUID orderID) throws CancelOrderException;

	public void addObserver(TradeListener listener);

	public void disconnect();

}
