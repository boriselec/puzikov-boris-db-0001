package com.acme.bankapp.domain.bank;

public class NotEnoughFundsException extends BankException{
	
	public NotEnoughFundsException() {
		super();
	}

	public NotEnoughFundsException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotEnoughFundsException(String message) {
		super(message);
	}

	public NotEnoughFundsException(Throwable cause) {
		super(cause);
	}

}
