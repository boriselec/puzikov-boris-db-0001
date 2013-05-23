package com.see.common.domain;

import java.util.Date;
import java.util.UUID;

public class Order {

	final private UUID orderID;
	final private String clientName;

	final private String stock;

	final private OrderType type;
	final private float price;
	final private int quantity;
	final private Date date;

	public Order(UUID orderID, String clientName, String stock,
			OrderType type, float price, int quantity, Date date) {
		this.orderID = orderID;
		this.clientName = clientName;
		this.stock = stock;
		this.type = type;
		this.price = price;
		this.quantity = quantity;
		this.date = date;
	}

	public UUID getOrderID() {
		return orderID;
	}

	public OrderType getType() {
		return type;
	}

	public float getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Date getDate() {
		return date;
	}

	public Order getPartiallyFilledOrder(int quantity) {
		return new Order(this.orderID, this.clientName, this.stock, this.type,
				this.price, this.quantity - quantity, this.date);
	}

	public String getStock() {
		return stock;
	}

	public String getClientName() {
		return clientName;
	}
}
