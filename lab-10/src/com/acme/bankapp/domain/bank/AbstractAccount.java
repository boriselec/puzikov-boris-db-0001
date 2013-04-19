package com.acme.bankapp.domain.bank;

public abstract class AbstractAccount implements Account {

	protected int id;
	protected double balance;

	public AbstractAccount(final int id, final double amount) {
		this.id = id;
		this.balance = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void deposit(double amount){
		this.balance += amount;
	}

	public abstract void withdraw(final double amount);

	public abstract double maximumAmountToWithdraw();
	

}
