package com.acme.bankapp.domain.bank;

public class OverDraftLimitExceededException extends NotEnoughFundsException{
	
	private Client client;
	private double maxAmount;
	public OverDraftLimitExceededException(double amount) {
		super(amount);
	}

	public OverDraftLimitExceededException(Client client, Throwable cause) {
		super(cause);
		this.client = client;
	}

	public OverDraftLimitExceededException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace, double amount) {
		super(message, cause, enableSuppression, writableStackTrace, amount);
	}

	public OverDraftLimitExceededException(String message, Throwable cause,
			double amount) {
		super(message, cause, amount);
		// TODO Auto-generated constructor stub
	}

	public OverDraftLimitExceededException(Throwable cause, double amount) {
		super(cause, amount);
		// TODO Auto-generated constructor stub
	}

	public OverDraftLimitExceededException(String string, double amount,
			double d) {
		super(string, amount);
		this.maxAmount = maxAmount;
		// TODO Auto-generated constructor stub
	}

}
