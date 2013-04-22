package com.acme.bankapp.domain.bank;

public class NegativeArgumentException extends BankException{

	public NegativeArgumentException() {
		super();
	}

	public NegativeArgumentException(String message, Throwable cause) {
		super(message, cause);
	}

	public NegativeArgumentException(String message) {
		super(message);
	}

	public NegativeArgumentException(Throwable cause) {
		super(cause);
	}


}
