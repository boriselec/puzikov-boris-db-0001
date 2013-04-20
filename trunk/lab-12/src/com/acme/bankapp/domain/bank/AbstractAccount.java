package com.acme.bankapp.domain.bank;

public abstract class AbstractAccount implements Account {

	protected int id;
	protected double balance;

	public AbstractAccount(final int id, final double amount) throws NegativeArgumentException {
		this.id = id;
		if (amount >=0){
			this.balance = amount;
		}
		else {
			throw new NegativeArgumentException("Account initialize error: Balance is negative");
		}
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount){
		this.balance += amount;
	}

	public abstract void withdraw(final double amount) throws NotEnoughFundsException;

	public abstract double maximumAmountToWithdraw();
	

}
