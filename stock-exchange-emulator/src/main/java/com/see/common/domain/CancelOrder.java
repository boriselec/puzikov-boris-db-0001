package com.see.common.domain;

import java.util.UUID;

public class CancelOrder extends Order {
	private static final long serialVersionUID = 1961991113923951371L;

	private TradeOperation type;
	private UUID cancelingOrderID;

	public CancelOrder(String login, String stockName, UUID orderID) {
		super(login, stockName);
		this.cancelingOrderID = orderID;
	}

	public TradeOperation getType() {
		return type;
	}

	public UUID getCancelingOrderID() {
		return cancelingOrderID;
	}

}
