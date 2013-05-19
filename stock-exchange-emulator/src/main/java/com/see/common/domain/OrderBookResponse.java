package com.see.common.domain;

public class OrderBookResponse {
	public OrderBookResponse(String... to) {
		this.to = to;
	}

	public String[] getTo() {
		return to;
	}

	private String[] to;
}
