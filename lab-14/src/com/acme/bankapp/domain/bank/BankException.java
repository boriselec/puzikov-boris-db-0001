package com.acme.bankapp.domain.bank;

public abstract class BankException extends Exception{

	public BankException() {
		super();
	}


	public BankException(String message, Throwable cause) {
		super(message, cause);
	}

	public BankException(String message) {
		super(message);
	}

	public BankException(Throwable cause) {
		super(cause);
	}
}
