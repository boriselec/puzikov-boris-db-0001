package com.see.common.domain;

public class ErrorResponse extends OrderBookResponse {
	public ErrorResponse(Order order, String message) {
		super(order.getLogin());
		this.message = message;
	}

	private String message;

	public String getMessage() {
		return message;
	}
}
