package com.acme.bankapp.domain.bank;

public interface Account {

	void deposit(double amount);

	void withdraw(double amount);
	
	double maximumAmountToWithdraw();

	double getBalance();
}
