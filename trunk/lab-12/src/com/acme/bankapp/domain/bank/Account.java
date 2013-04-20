package com.acme.bankapp.domain.bank;

public interface Account {

	void deposit(double amount);

	void withdraw(double amount) throws NotEnoughFundsException;
	
	double maximumAmountToWithdraw();

	double getBalance();
	
	long decimalValue();
}
