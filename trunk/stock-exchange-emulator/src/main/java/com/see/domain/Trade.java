package com.see.domain;

public class Trade extends OrderBookResponse {
	public Trade(TradeOrder bid, TradeOrder offer, float price, int shares) {
		super(bid.getLogin(), offer.getLogin());
		this.bid = bid;
		this.offer = offer;
		this.price = price;
		this.shares = shares;
	}

	private final TradeOrder bid;
	private final TradeOrder offer;
	private final float price;
	private final int shares;

	public TradeOrder getBid() {
		return bid;
	}

	public TradeOrder getOffer() {
		return offer;
	}

	public float getPrice() {
		return price;
	}

	public int getShares() {
		return shares;
	}
}
