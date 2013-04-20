package com.acme.bankapp.domain.bank;


public class ParseFeedException extends BankException{

	public ParseFeedException() {
		super();
	}

	public ParseFeedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseFeedException(String message) {
		super(message);
	}

	public ParseFeedException(Throwable cause) {
		super(cause);
	}

}
