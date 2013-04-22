package com.acme.mock;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.service.bank.BankApplication;

public interface Command {
	public static final Command CLOSE_CONNECTION_COMMAND = null;
	String execute(Bank bank);
}
