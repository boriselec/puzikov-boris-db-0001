package com.acme.bankapp.service.bank;

import java.awt.image.BandCombineOp;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.stream.FileImageInputStream;
import javax.sound.sampled.Line;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.CheckingAccount;
import com.acme.bankapp.domain.bank.Client;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.Gender;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.NotEnoughFundsException;
import com.acme.bankapp.domain.bank.OverDraftLimitExceededException;
import com.acme.bankapp.domain.bank.SavingAccount;

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
	
	public void loadFeed(Bank bank, LineNumberReader lnr) throws IOException, ClientExistsException, NegativeArgumentException, ParseFeedException{
		
		String line;
		do {
			line = lnr.readLine();
			if (line == null)
				break;
			Scanner scanner = new Scanner(line);
			scanner.useDelimiter(";");
		
			String name = "";
			String gender = "";
			String accountType = "";
			double balance = -1;
			double overdraft = -1;
			while (scanner.hasNext()){
				String current = scanner.next();
				char first = current.charAt(0);
				switch (first) {
				case 'a':
					accountType = current.substring(current.indexOf('=') + 1);
					
					break;
				case 'b':
					balance = Double.parseDouble(current.substring(current.indexOf('=') + 1));
					
					break;
				case 'o':
					overdraft = Double.parseDouble(current.substring(current.indexOf('=') + 1));
					
					break;
				case 'n':
					name = current.substring(current.indexOf('=') + 1);
					
					break;
				case 'g':
					gender = current.substring(current.indexOf('=') + 1);
					
					break;

				default:
					throw new ParseFeedException();
				}
			}
			if ("c".equals(accountType))
				bank.addClient(new Client(name, Gender.getByChar(gender), new CheckingAccount(balance, overdraft)));
			else if ("s".equals(accountType)){
				bank.addClient(new Client(name, Gender.getByChar(gender), new SavingAccount(balance)));
			}
			else {
					throw new ParseFeedException();
			}
		} while (line != null);
	}
	
	public void saveBank(Bank bank){
		
	}
}
