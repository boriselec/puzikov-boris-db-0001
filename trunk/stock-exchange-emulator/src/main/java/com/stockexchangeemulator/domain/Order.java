package com.stockexchangeemulator.domain;

public class Order {
	public Order(String stockName, Type operation, int sharesCount, float price) {
		this.stockName = stockName;
		this.operation = operation;
		this.sharesCount = sharesCount;
		this.price = price;
	}

	private String stockName;
	private Type operation;
	private int sharesCount;
	private float price;

	public float getPrice() {
		return price;
	}

	public int getSharesCount() {
		return sharesCount;
	}

	public Type getType() {
		return operation;
	}

	public String getStockName() {
		return stockName;
	}

	public void partlyFill(int amount) {
		sharesCount -= amount;
	}
}
