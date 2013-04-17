package com.acme.bankapp.domain.bank;

public class NegativeArgumentException extends BankException{

	public NegativeArgumentException(String message) {
		super(message);
	}

}
