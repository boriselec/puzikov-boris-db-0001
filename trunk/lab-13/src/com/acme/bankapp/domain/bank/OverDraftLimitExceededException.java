package com.acme.bankapp.domain.bank;

public class OverDraftLimitExceededException extends NotEnoughFundsException{

	public OverDraftLimitExceededException() {
		super();
	}

	public OverDraftLimitExceededException(String message, Throwable cause) {
		super(message, cause);
	}

	public OverDraftLimitExceededException(String message) {
		super(message);
	}

	public OverDraftLimitExceededException(Throwable cause) {
		super(cause);
	}
	
}
