package com.acme.bank;

public abstract class AbstractAccount {

	protected int id;
	protected double balance;

	public AbstractAccount(final double amount) {
		this.balance = amount;
	}

	public double getBalance() {
		return balance;
	}

	public abstract void deposit(final double amount);

	public abstract void withdraw(final double amount);

}
