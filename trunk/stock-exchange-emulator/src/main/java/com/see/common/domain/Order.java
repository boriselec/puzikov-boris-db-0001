package com.see.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class Order implements Serializable {

	private static final long serialVersionUID = 6690070311631397742L;

	public Order(String login, String stockName) {
		this.login = login;
		this.stockName = stockName;
	}

	private final String stockName;
	private final String login;
	private Date date;
	private UUID orderID;

	public void setOrderID(UUID orderID) {
		this.orderID = orderID;
	}

	private Integer localOrderID;

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

	public Integer getLocalOrderID() {
		return localOrderID;
	}

	public void setLocalOrderID(Integer id) {
		this.localOrderID = id;
	}

}
