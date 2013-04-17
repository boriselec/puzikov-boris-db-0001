package com.acme.bankapp.domain.bank;

public interface Account {

	void deposit(double amount) throws NegativeArgumentException;

	void withdraw(double amount) throws NotEnoughFundsException, NegativeArgumentException;
	
	double maximumAmountToWithdraw();

}
