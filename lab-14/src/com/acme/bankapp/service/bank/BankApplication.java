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
	private final static int port = 2006;

	public static void main(String[] args) {
		try {

			Bank bank = createTestBank();
			BankService service = new BankService();

			boolean isServer = false;
			for (int i = 0; i < args.length; i++) {

				switch (args[i]) {
				case "-loadfeed":
					if (i + 1 == args.length)
						throw new IllegalArgumentException();
					loadFeedFile(bank, args[++i]);
					break;
				case "-loadbank":
					if (i + 1 == args.length)
						throw new IllegalArgumentException();
					bank = service.readBank(args[++i]);
					break;
				case "-server":
					isServer = true;
					break;
				case "-client":
					createClient();
					break;

				default:
					String errorMessage = String
							.format("Parse Args Error: %s is not valid command-line argument",
									args[i]);
					throw new IllegalArgumentException(errorMessage);
				}
			}
			if (isServer == true)
				createServer(bank);
			else {
				bank = createTestBank();
				testCycle(bank);
			}

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

	private static void createClient() {
		String portString = new Integer(port).toString();
		BankClient.main(new String[] { portString });
	}

	private static void loadFeedFile(Bank bank, String path)
			throws ParseFeedException, ClientExistsException,
			NegativeArgumentException, IOException {
		FileReader fReader = new FileReader(path);
		LineNumberReader lnr = new LineNumberReader(fReader);
		try {
			BankDataLoaderService feedLoaderService = new BankDataLoaderService();
			feedLoaderService.loadFeed(bank, lnr);
		} finally {
			try {
				lnr.close();
				fReader.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private static void createServer(Bank bank) {
		String portString = new Integer(port).toString();
		if (bank == null) {
			BankServer.main(new String[] { portString });
		} else {
			BankServer server = new BankServer(bank, port);
			server.startServer();
		}
	}

	private static Bank createTestBank() {
		Bank bank = new Bank(new ClientRegistrationListener() {
			private static final long serialVersionUID = 7782830221346863755L;

			@Override
			public void onClientAdded(Client client) {
				System.out.println(client.getSalutation());

			}
		}, new ClientRegistrationListener() {
			private static final long serialVersionUID = -6496994804997142949L;

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Notification for client %s to be sent\n",
						client.getSalutation());

			}
		}, new ClientRegistrationListener() {
			private static final long serialVersionUID = 338850538707125656L;

			@Override
			public void onClientAdded(Client client) {
				System.out.format("Client: %s, Date: %s\n",
						client.getSalutation(), new Date());

			}
		});
		return bank;
	}

	private static void testCycle(Bank bank) throws ClientExistsException,
			NegativeArgumentException, NotEnoughFundsException, IOException,
			ClassNotFoundException {

		BankService service = new BankService();
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
