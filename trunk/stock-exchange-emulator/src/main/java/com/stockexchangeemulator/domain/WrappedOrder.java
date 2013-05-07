package com.stockexchangeemulator.domain;

import java.util.Date;

public class WrappedOrder {
	public WrappedOrder(String login, int orderID, Order order, Date date) {
		super();
		this.login = login;
		this.orderID = orderID;
		this.order = order;
		this.date = date;
	}

	private String login;
	private int orderID;
	private Order order;
	private Date date;

	public Order getOrder() {
		return order;
	}

	public String getClientID() {
		return login;
	}

	public int getOrderID() {
		return orderID;
	}

	public Date getDate() {
		return date;
	}
}
