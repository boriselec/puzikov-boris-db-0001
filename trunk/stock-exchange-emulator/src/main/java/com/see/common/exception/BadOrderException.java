package com.see.common.exception;

import java.util.UUID;

@SuppressWarnings("serial")
public class BadOrderException extends Throwable {

	private final UUID orderID;

	public BadOrderException(String string) {
		this(null, string);
	}

	public BadOrderException(UUID orderID, String format) {
		super(format);
		this.orderID = orderID;
	}

	public BadOrderException() {
		this(null, null);
	}

	public UUID getOrderID() {
		return orderID;
	}

}
