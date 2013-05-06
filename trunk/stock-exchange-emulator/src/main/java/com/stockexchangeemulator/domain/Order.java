package com.stockexchangeemulator.domain;

import java.io.Serializable;

public class Order implements Serializable {

	private static final long serialVersionUID = 6690070311631397742L;

	public Order(int clientID, String stockName, Operation operation,
			int sharesCount, float price) {
		this.clientID = clientID;
		this.stockName = stockName;
		this.operation = operation;
		this.sharesCount = sharesCount;
		this.price = price;
	}

	public Order(String stockName, Operation operation, int previousOrderID) {
		this.stockName = stockName;
		this.operation = operation;
		this.previousOrderID = previousOrderID;
	}

	private String stockName;
	private Operation operation;
	private int sharesCount;
	private float price;
	private int previousOrderID;
	private int clientID;

	public float getPrice() {
		return price;
	}

	public int getSharesCount() {
		return sharesCount;
	}

	public Operation getType() {
		return operation;
	}

	public String getStockName() {
		return stockName;
	}

	public void partliallyFill(int amount) {
		sharesCount -= amount;
	}

	public int getOrderID() {
		return previousOrderID;
	}

	public int getClientID() {
		return clientID;
	}

}
