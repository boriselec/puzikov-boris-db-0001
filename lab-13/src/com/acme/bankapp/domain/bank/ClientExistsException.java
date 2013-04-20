package com.acme.bankapp.domain.bank;

public class ClientExistsException extends BankException{

	public ClientExistsException() {
		super();
	}


	public ClientExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ClientExistsException(String message) {
		super(message);
	}

	public ClientExistsException(Throwable cause) {
		super(cause);
	}

}
