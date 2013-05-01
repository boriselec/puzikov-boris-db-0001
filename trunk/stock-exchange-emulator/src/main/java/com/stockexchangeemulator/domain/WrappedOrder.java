package com.stockexchangeemulator.domain;

import java.util.Date;

public class WrappedOrder {
	public WrappedOrder(int clientID, int orderID, Order order, Date date) {
		super();
		this.clientID = clientID;
		this.orderID = orderID;
		this.order = order;
		this.date = date;
	}

	private int clientID;
	private int orderID;
	private Order order;
	private Date date;

	public Order getOrder() {
		return order;
	}

	public int getClientID() {
		return clientID;
	}

	public int getOrderID() {
		return orderID;
	}

	public Date getDate() {
		return date;
	}
}
