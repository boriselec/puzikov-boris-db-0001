package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;

public class BankApplication {
	public static void main(String[] args) {
		try {
			BankService service = new BankService();
			Bank bank = new Bank(new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.println(client.getSalutation());

				}
			}, new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.format(
							"Notification for client %s to be sent\n", client.getSalutation());

				}
			}, new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.format("Client: %s, Date: %s\n", client.getSalutation(),
							new Date());

				}
			});

			for (int i = 0; i < args.length - 1; i++){
				if ("-loadfeed".equals(args[i])) {
					FileReader fReader = new FileReader(args[i + 1]);
					LineNumberReader lnr = new LineNumberReader(fReader);
					BankDataLoaderService feedLoaderService = new BankDataLoaderService();
					feedLoaderService.loadFeed(bank, lnr);
					break;

				}
				if ("-loadbank".equals(args[i])) {
					bank = service.readBank(args[i + 1]);
					break;
				}
				
			}

			service.printBalance(bank);

			service.addClient(bank, new Client("Bob", Gender.MALE,
					new CheckingAccount(1, 2.0, 3.0)));
			service.printBalance(bank);

			service.addClient(bank, new Client("Alice", Gender.FEMALE,
					new SavingAccount(2, 4.0)));
			service.printBalance(bank);

			service.modifyBank(bank);
			service.printBalance(bank);

			service.printMaximumAmountToWithdraw(bank);
			
		} catch (BankException ex) {
			System.out.println(ex.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
