package com.stockexchangeemulator.domain;

public class Order {
	public Order(String stockName, Type operation, int sharesCount, float price) {
		this.stockName = stockName;
		this.operation = operation;
		this.sharesCount = sharesCount;
		this.price = price;
	}

	public Order(String stockName, Type operation, int previousOrderID) {
		this.stockName = stockName;
		this.operation = operation;
		this.previousOrderID = previousOrderID;
	}

	private String stockName;
	private Type operation;
	private int sharesCount;
	private float price;
	private int previousOrderID;

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

	public int getOrderID() {
		return previousOrderID;
	}
}
