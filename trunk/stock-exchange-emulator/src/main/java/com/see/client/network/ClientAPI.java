package com.see.client.network;

import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.NoLoginException;

public interface ClientAPI {
	public void login(String loginName) throws NoLoginException;

	public UUID sendOrder(Order order) throws BadOrderException,
			NoLoginException;

	public void addObserver(ResponseObserver observer);

	public void disconnect();

}
