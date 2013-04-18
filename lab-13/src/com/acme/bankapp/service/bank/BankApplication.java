package com.acme.bankapp.service.bank;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.util.Date;

import javax.imageio.stream.FileImageInputStream;

import com.acme.bankapp.domain.bank.*;

public class BankApplication {
	public static void main(String[] args) {
		try {
			BankService service = new BankService();
			Bank bank = new Bank(new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.println(client);

				}
			}, new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.format(
							"Notification for client %s to be sent\n", client);

				}
			}, new ClientRegistrationListener() {

				@Override
				public void onClientAdded(Client client) {
					System.out.format("Client: %s, Date: %s\n", client,
							new Date());

				}
			});
			if (args.length > 1) {
				if ("-loadfeed".equals(args[0])) {
					FileReader fReader = new FileReader(args[1]);
					LineNumberReader lnr = new LineNumberReader(fReader);
					service.loadFeed(bank, lnr);

				}
				if ("-loadbank".equals(args[0])) {
					
				}
			}
			service.printBalance(bank);

			service.addClient(bank, new Client("Bob", Gender.MALE,
					new CheckingAccount(2.0, 3.0)));
			service.printBalance(bank);

			service.addClient(bank, new Client("Alice", Gender.FEMALE,
					new SavingAccount(4.0)));
			service.printBalance(bank);

			service.modifyBank(bank);
			service.printBalance(bank);

			service.printMaximumAmountToWithdraw(bank);
		} catch (BankException ex) {
			System.out.println(ex.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
