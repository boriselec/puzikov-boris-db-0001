package com.see.common.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class ClientResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6816711724699067797L;

	public ClientResponse(TradeOrder order, Status status) {
		this.login = order.getLogin();
		this.orderID = order.getOrderID();
		this.status = status;
		this.price = order.getPrice();
		this.dealPrice = order.getMeanDealPrice();
		this.requestedShares = order.getRequestedSharesCount();
		this.tradedShares = order.getTradedSharesCount();
		this.date = new Date();
		this.symbol = order.getStockName();
		this.operation = order.getType();
	}

	public ClientResponse(Order order, String errorMessage) {
		this.date = order.getDate();
		this.symbol = order.getStockName();
		this.orderID = order.getOrderID();
		this.login = order.getLogin();
		this.status = Status.ERROR;
		this.message = errorMessage;
	}

	private String login;
	private Status status;
	private float price;
	private float dealPrice;
	private int tradedShares;
	private int requestedShares;
	private String message;
	private Date date;
	private UUID orderID;
	private String symbol;
	private TradeOperation operation;

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

	public UUID getOrderID() {
		return orderID;
	}

	public ClientResponse splice(ClientResponse response2) {
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

	public float getDealPrice() {
		return dealPrice;
	}

	public TradeOperation getTradeOperation() {
		return operation;
	}

}
