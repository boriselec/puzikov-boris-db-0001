package com.see.client.service.api;

import java.util.UUID;

import com.see.client.service.exception.BadOrderException;
import com.see.client.service.exception.NoLoginException;
import com.see.domain.Order;

public interface ServerSession {
	public void login(String loginName) throws NoLoginException;

	public UUID sendOrder(Order order) throws BadOrderException,
			NoLoginException;

	public void addObserver(ResponseObserver observer);

}
