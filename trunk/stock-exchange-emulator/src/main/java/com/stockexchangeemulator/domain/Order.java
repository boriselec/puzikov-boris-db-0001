package com.stockexchangeemulator.domain;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

	private static final long serialVersionUID = 6690070311631397742L;

	public Order(String login, String stockName, Operation operation,
			int sharesCount, float price) {
		this.login = login;
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
	private String login;
	private Date date;
	private int orderID;

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
		return orderID;
	}

	public String getLogin() {
		return login;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getpreviousOrderID() {
		return this.previousOrderID;
	}

	public void setPreviousOrderID(int orderID2) {
		this.previousOrderID = orderID2;
	}

}
