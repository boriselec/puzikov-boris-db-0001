package com.acme.mock;

import com.acme.bankapp.domain.bank.Bank;

public class BankReport {
	public String execute(Bank bank, Command command){
		return command.execute(bank);
	}

}
