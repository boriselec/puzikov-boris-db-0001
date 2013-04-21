package com.acme.bankapp.service.bank;

import com.acme.bankapp.domain.bank.*;
import com.acme.mock.BankClient;
import com.acme.mock.BankServer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Date;

public class BankApplication {
	public static void main(String[] args) {
		try {
			int port = 2006;
			String portString = new Integer(port).toString();

			BankService service = new BankService();
			Bank bank = createTestBank();

			for (int i = 0; i < args.length; i++) {
				if ("-loadfeed".equals(args[i])) {
					if (i + 1 < args.length) {
						FileReader fReader = new FileReader(args[i + 1]);
						LineNumberReader lnr = new LineNumberReader(fReader);
						BankDataLoaderService feedLoaderService = new BankDataLoaderService();
						feedLoaderService.loadFeed(bank, lnr);
						lnr.close();
						fReader.close();
						i += 1;
						continue;
					}
				}
				if ("-loadbank".equals(args[i])) {
					if (i + 1 < args.length) {
						bank = service.readBank(args[i + 1]);
						i += 1;
						continue;
					}
				}
				if ("-client".equals(args[i])) {
					BankClient.main(new String[] { portString });
				}
				if ("-server".equals(args[i])) {
					if (bank == null) {
						BankServer.main(new String[] { portString });
					}
					else{
						BankServer server = new BankServer(bank, port);
						service.printBalance(bank);
						server.startServer();
						
					}
				}
			}
			bank = createTestBank();
			testCycle(service, bank);

		} catch (BankException ex) {
			System.out.println(ex.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static Bank createTestBank() {
		Bank bank = new Bank(new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.println(client.getSalutation());

			}
		}, new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Notification for client %s to be sent\n",
						client.getSalutation());

			}
		}, new ClientRegistrationListener() {

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Client: %s, Date: %s\n",
						client.getSalutation(), new Date());

			}
		});
		return bank;
	}

	public static void testCycle(BankService service, Bank bank)
			throws ClientExistsException, NegativeArgumentException,
			NotEnoughFundsException, IOException, ClassNotFoundException {

		service.printBalance(bank);

		service.addClient(bank, new Client("Bob", Gender.MALE,
				new CheckingAccount(bank.getID(), 2.0, 3.0)));
		service.printBalance(bank);

		service.addClient(bank, new Client("Alice", Gender.FEMALE,
				new SavingAccount(bank.getID(), 4.0)));
		service.printBalance(bank);

		service.modifyBank(bank);
		service.printBalance(bank);

		service.printMaximumAmountToWithdraw(bank);

		service.saveBank(bank);
		service.reset(bank);
		service.printBalance(bank);
		bank = service.readBank();
		service.printBalance(bank);

	}

}
