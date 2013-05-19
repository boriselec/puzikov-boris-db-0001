package com.see.common.exception;

@SuppressWarnings("serial")
public class DisconnectException extends Exception {
	public DisconnectException(String message) {
		super(message);
	}

	public DisconnectException() {
		super();
	}

}
