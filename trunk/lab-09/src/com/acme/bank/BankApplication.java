package com.acme.bank;

public class BankApplication {
	public static void main(String[] args) {
		Bank bank = new Bank();
		bank.printBalance();
		bank.addClient(new Client("Bob", Gender.MALE, new Account(2.0)));
		bank.printBalance();
		bank.addClient(new Client("Alice", Gender.FEMALE, new Account(4.0)));
		bank.printBalance();
		bank.modifyBank();
		bank.printBalance();
		
	}

}
