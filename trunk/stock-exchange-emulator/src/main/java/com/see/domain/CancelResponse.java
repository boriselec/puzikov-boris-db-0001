package com.see.domain;

public class CancelResponse extends OrderBookResponse {

	public CancelResponse(TradeOrder order) {
		super(order.getLogin());
		this.order = order;
	}

	public TradeOrder getOrder() {
		return order;
	}

	private TradeOrder order;

}
