package com.stockexchangeemulator.domain;

public class TradeOrder extends Order {
	private static final long serialVersionUID = 2584635612280871492L;

	private TradeOperation type;
	private int sharesCount;
	private float price;

	public TradeOrder(String login, String stockName, TradeOperation type,
			int sharesCount, float price) {
		super(login, stockName);
		this.type = type;
		this.sharesCount = sharesCount;
		this.price = price;
	}

	public void partliallyFill(int amount) {
		sharesCount -= amount;
	}

	public TradeOperation getType() {
		return type;
	}

	public int getSharesCount() {
		return sharesCount;
	}

	public float getPrice() {
		return price;
	}

}
