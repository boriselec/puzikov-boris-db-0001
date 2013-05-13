package com.stockexchangeemulator.stockexchange.stockexchange;

@SuppressWarnings("serial")
public class DisconnectException extends Exception {
	public DisconnectException(String message) {
		super(message);
	}

	public DisconnectException() {
		super();
	}

}
