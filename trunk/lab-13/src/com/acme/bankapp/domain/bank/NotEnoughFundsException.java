package com.acme.bankapp.domain.bank;

public class NotEnoughFundsException extends BankException{
	

	private double amount;

	public NotEnoughFundsException(double amount) {
		super();
		// TODO Auto-generated constructor stub
	}

	public NotEnoughFundsException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace, double amount) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughFundsException(String message, Throwable cause, double amount) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughFundsException(String message, double amount) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughFundsException(Throwable cause, double amount) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public NotEnoughFundsException(Throwable cause) {
		// TODO Auto-generated constructor stub
	}

}
