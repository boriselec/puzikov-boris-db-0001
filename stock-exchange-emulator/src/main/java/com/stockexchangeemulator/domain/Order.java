package com.stockexchangeemulator.domain;

import java.io.Serializable;
import java.util.Date;

public abstract class Order implements Serializable {

	private static final long serialVersionUID = 6690070311631397742L;

	public Order(String login, String stockName) {
		this.login = login;
		this.stockName = stockName;
	}

	private String stockName;
	private String login;
	private Date date;
	private int orderID;

	public String getStockName() {
		return stockName;
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

	public int getSharesCount() {
		return 0;
	}

	public int getCancelingOrderID() {
		return this.orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

}
