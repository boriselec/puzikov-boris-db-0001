package com.stockexchangeemulator.domain;

import java.util.Date;

public class Response {

	public Response(WrappedOrder wrappedOrder, Status status, float price,
			int sharesCount, Date date) {
		this.clientID = wrappedOrder.getClientID();
		this.status = status;
		this.price = price;
		this.tradedShares = sharesCount;
		this.requestedShares = wrappedOrder.getOrder().getSharesCount();
		this.message = "Ok";
		this.date = date;
		this.orderID = wrappedOrder.getOrderID();
	}

	private int clientID;
	private Status status;
	private float price;
	private int tradedShares;
	private int requestedShares;
	private String message;
	private Date date;
	private int orderID;

	public Status getStatus() {
		return status;
	}

	public float getPrice() {
		return price;
	}

	public int getTradedShares() {
		return tradedShares;
	}

	public String getMessage() {
		return message;
	}

	public int getClientID() {
		return clientID;
	}

	public Date getDate() {
		return date;
	}

	public int getOrderID() {
		return orderID;
	}

	public Response splice(Response response2) {
		this.tradedShares += response2.tradedShares;
		if (response2.status == Status.FULLY_FILLED)
			this.status = Status.FULLY_FILLED;
		return this;
	}

}
