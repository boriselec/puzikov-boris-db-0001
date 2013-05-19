package com.see.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Order implements Serializable {

	private static final long serialVersionUID = 6690070311631397742L;

	public Order(String login, String stockName) {
		this.orderID = UUID.randomUUID();
		this.login = login;
		this.stockName = stockName;
	}

	private final String stockName;
	private final String login;
	private Date date;
	private final UUID orderID;
	private UUID localOrderID;

	public String getStockName() {
		return stockName;
	}

	public String getLogin() {
		return login;
	}

	public UUID getCancelingOrderID() {
		return this.orderID;
	}

	public UUID getOrderID() {
		return this.orderID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public UUID getLocalOrderID() {
		return localOrderID;
	}

	public void setLocalOrderID(UUID id) {
		this.localOrderID = id;
	}

}
