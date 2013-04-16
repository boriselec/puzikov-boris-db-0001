package com.acme.bankapp.domain.bank;

public class SavingAccount extends AbstractAccount{
	private double balance;

	public double getBalance() {
		return balance;
	}

	public SavingAccount(double d) {
		super(d);
		this.balance = d;
	}

	public void deposit(double amount){
		this.balance += amount;
	}

	public void withdraw(double amount){
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else {
			//throw
		}

	}
		@Override
	public double maximumAmountToWithdraw() {
		return balance;
		
	}
	

}
