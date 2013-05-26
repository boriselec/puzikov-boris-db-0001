package com.see.common.exception;

import java.util.UUID;

@SuppressWarnings("serial")
public class CancelOrderException extends Exception {

	public CancelOrderException(UUID orderID, String message) {
		super(message);
		this.orderID = orderID;
	}

	private final UUID orderID;

	public UUID getOrderID() {
		return orderID;
	}

}
