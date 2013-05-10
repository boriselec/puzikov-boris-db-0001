package com.stockexchangeemulator.client.service.exception;

@SuppressWarnings("serial")
public class BadOrderException extends Throwable {

	public BadOrderException() {
		super();
	}

	public BadOrderException(String string) {
		super(string);
	}

}
