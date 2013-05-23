package com.see.common.domain;

public class Trade {
	public Trade(Order bid, Order offer, float price, int shares) {
		this.bid = bid;
		this.offer = offer;
		this.price = price;
		this.shares = shares;
	}

	private final Order bid;
	private final Order offer;
	private final float price;
	private final int shares;

	public Order getBid() {
		return bid;
	}

	public Order getOffer() {
		return offer;
	}

	public float getPrice() {
		return price;
	}

	public int getShares() {
		return shares;
	}
}
