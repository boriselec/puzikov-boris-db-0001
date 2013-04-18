package com.acme.bankapp.domain.bank;

public abstract class BankException extends Exception{

	public BankException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BankException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public BankException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public BankException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public BankException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}


	
	
}
