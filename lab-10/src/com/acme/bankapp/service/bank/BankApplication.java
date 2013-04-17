package com.acme.bankapp.service.bank;
import java.util.Date;

import com.acme.bankapp.domain.bank.*;

public class BankApplication {
	public static void main(String[] args) {
		BankService service = new BankService();
		Bank bank = new Bank(
			new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.println(client);

			}
		},
			new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Notification for client %s to be sent\n", client);

			}
		},
			new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Client: %s, Date: %s\n", client, new Date());

			}
		}
			);
		service.printBalance(bank);
		
		service.addClient(bank, new Client("Bob", Gender.MALE, new CheckingAccount(2.0, 3.0)));
		service.printBalance(bank);
		
		service.addClient(bank, new Client("Alice", Gender.FEMALE, new SavingAccount(4.0)));
		service.printBalance(bank);
		
		service.modifyBank(bank);
		service.printBalance(bank);
		
		service.printMaximumAmountToWithdraw(bank);
		
	}

}
