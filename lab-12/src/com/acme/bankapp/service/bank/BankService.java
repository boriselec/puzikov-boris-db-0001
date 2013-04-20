package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.Client;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.NotEnoughFundsException;
import com.acme.bankapp.domain.bank.OverDraftLimitExceededException;

public class BankService {
	
	private final static String EMPTY_BANK = 
			"Bank is empty\n";
	private final static String BANK_HEADER = 
			"Bank\n************\n";

	public void addClient(Bank bank, Client client) throws ClientExistsException{
		bank.addClient(client);
		;
	}
	public void printBalance(Bank bank){
		String toPrint = "";
		if (bank.getNumOfClients() == 0){
			toPrint += EMPTY_BANK;
		}
		else {
			toPrint += BANK_HEADER;
			for (Client client : bank.getClients()){
				if (client == null)
					continue;
				else {
					toPrint = toPrint + client.getSalutation() + ": " + 
							client.getAccounts().getBalance() + '\n';
				}
			}
		}
			toPrint += "\n";
		System.out.println(toPrint);
	}
	public void modifyBank(Bank bank) throws NotEnoughFundsException{
		Client[] clients = bank.getClients();
		if (clients[0] != null){
			clients[0].getAccounts().deposit(3.0);
		}
		if (clients[1] != null){
			clients[1].getAccounts().withdraw(3.0);
		}
	}
	
	public void printMaximumAmountToWithdraw(Bank bank){
		String toPrint = "";
		if (bank.getNumOfClients() == 0){
			toPrint += EMPTY_BANK;
		}
		else {
			toPrint += BANK_HEADER;
			for (Client client : bank.getClients()){
				if (client == null)
					continue;
				else {
					toPrint = toPrint + client.getSalutation() + ": " + 
							client.getAccounts().maximumAmountToWithdraw() + '\n';
				}
			}
		}
			toPrint += "\n";
			System.out.println(toPrint);
	}
}
