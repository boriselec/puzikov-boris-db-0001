package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.Client;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.NotEnoughFundsException;
import com.acme.bankapp.domain.bank.OverDraftLimitExceededException;

public class BankService {

	public void addClient(Bank bank, Client client) throws ClientExistsException{
		bank.addClient(client);
		;
	}
	public void printBalance(Bank bank){
		String toPrint = "";
		if (bank.getNumOfClients() == 0){
			toPrint += "Bank is empty\n";
		}
		else {
			toPrint += "Bank\n";
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
	public void modifyBank(Bank bank) throws NotEnoughFundsException, NegativeArgumentException{
		Client[] clients = bank.getClients();
		if (clients[0] != null){
			clients[0].getAccounts().deposit(3.0);
		}
		if (clients[1] != null){
			try{
				clients[1].getAccounts().withdraw(9.0);
			}
			catch(OverDraftLimitExceededException e){
				throw new OverDraftLimitExceededException(clients[1], e);
			}
		}
	}
	
	public void printMaximumAmountToWithdraw(Bank bank){
		String toPrint = "";
		if (bank.getNumOfClients() == 0){
			toPrint += "Bank is empty\n";
		}
		else {
			toPrint += "Bank\n";
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
