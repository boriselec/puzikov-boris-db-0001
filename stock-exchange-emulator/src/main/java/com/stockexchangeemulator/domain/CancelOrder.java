package com.stockexchangeemulator.domain;

public class CancelOrder extends Order {
	private static final long serialVersionUID = 1961991113923951371L;

	private TradeOperation type;
	private int cancelingOrderID;

	public CancelOrder(String login, String stockName, int orderID) {
		super(login, stockName);
		this.cancelingOrderID = orderID;
	}

	public TradeOperation getType() {
		return type;
	}

	public int getCancelingOrderID() {
		return cancelingOrderID;
	}

}
