package com.see.common.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class TradeResponse extends Message implements Serializable {
	private static final long serialVersionUID = 8728026755141535281L;

	final private UUID orderID;
	final private String counterpart;
	final private float price;
	final private int quantity;
	final private Date date;

	public TradeResponse(UUID orderID, String counterpart, float price,
			int quantity) {
		this.orderID = orderID;
		this.counterpart = counterpart;
		this.price = price;
		this.quantity = quantity;
		this.date = new Date();
	}

	public String getCounterpart() {
		return counterpart;
	}

	public float getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public UUID getOrderID() {
		return orderID;
	}

	public Date getDate() {
		return date;
	}

}
