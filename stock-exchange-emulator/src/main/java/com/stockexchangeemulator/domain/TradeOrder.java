package com.stockexchangeemulator.domain;

public class TradeOrder extends Order {
	private static final long serialVersionUID = 2584635612280871492L;

	private TradeOperation type;
	private int requestedSharesCount;
	private int tradedSharesCount;
	private float price;
	private float meanDealPrice;

	public TradeOrder(String login, String stockName, TradeOperation type,
			int sharesCount, float price) {
		super(login, stockName);
		this.type = type;
		this.requestedSharesCount = sharesCount;
		this.tradedSharesCount = 0;
		this.price = price;
		this.meanDealPrice = 0;
	}

	public void fullyFill(float price) {
		int amount = requestedSharesCount - tradedSharesCount;
		this.meanDealPrice = (price * amount + this.meanDealPrice
				* tradedSharesCount)
				/ (amount + this.tradedSharesCount);
		System.out.println(this.tradedSharesCount);
		System.out.println(amount);
		this.tradedSharesCount = this.requestedSharesCount;
	}

	public void partliallyFill(float price, int amount) {
		this.meanDealPrice = (price * amount + this.meanDealPrice
				* tradedSharesCount)
				/ (amount + this.tradedSharesCount);
		System.out.println("p");
		System.out.println(this.tradedSharesCount);
		System.out.println(amount);
		this.tradedSharesCount += amount;
	}

	public TradeOperation getType() {
		return type;
	}

	public int getSharesCount() {
		return this.requestedSharesCount - this.tradedSharesCount;
	}

	public int getRequestedSharesCount() {
		return this.requestedSharesCount;
	}

	public float getPrice() {
		return price;
	}

	public int getTradedSharesCount() {
		return tradedSharesCount;
	}

	public float getMeanDealPrice() {
		return meanDealPrice;
	}

}
