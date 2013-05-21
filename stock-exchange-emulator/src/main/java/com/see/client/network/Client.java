package com.see.client.network;

import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.NoLoginException;

public interface Client {
	public void login(String loginName) throws NoLoginException;

	public UUID sendOrder(Order order) throws BadOrderException,
			NoLoginException;

	public void cancelOrder(Order order) throws CancelOrderException;

	public void addObserver(ResponseObserver observer);

	public void disconnect();

}
