package com.stockexchangeemulator.domain;

import java.io.Serializable;
import java.util.Date;

public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6816711724699067797L;

	public Response(Order order, Status status, String message, float price,
			int sharesCount, Date date) {
		this.login = order.getLogin();
		this.status = status;
		this.price = price;
		this.tradedShares = sharesCount;
		this.requestedShares = order.getSharesCount();
		this.message = message;
		this.date = date;
		this.orderID = order.getOrderID();
		this.symbol = order.getStockName();
	}

	private String login;
	private Status status;
	private float price;
	private int tradedShares;
	private int requestedShares;
	private String message;
	private Date date;
	private int orderID;
	private String symbol;

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

	public String getLogin() {
		return login;
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

	public String getSymbol() {
		return symbol;
	}

	public int getRequestedShares() {
		return requestedShares;
	}

}
