package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.Client;

public class BankService {

	public void addClient(Bank bank, Client client){
		bank.addClient(client);
		;
	}
	public void printBalance(Bank bank){
		System.out.println(bank);
	}
	public void modifyBank(Bank bank){
		bank.modifyBank();
	}
	
	public void printMaximumAmountToWithdraw(Bank bank){
		bank.printMaximumAmountToWithdraw();
	}
}
