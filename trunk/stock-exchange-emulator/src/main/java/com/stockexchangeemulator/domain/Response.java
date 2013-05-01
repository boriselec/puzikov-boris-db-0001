package com.stockexchangeemulator.domain;

public class Response {
	public Response(WrappedOrder wrappedOrder, Status status) {
		// TODO Auto-generated constructor stub
	}

	private int clientID;
	private Status status;
	private float price;
	private int tradedShares;
	private String message;

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
}
