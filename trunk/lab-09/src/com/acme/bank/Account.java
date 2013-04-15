package com.acme.bank;

public class Account {
	private double balance;

	public double getBalance() {
		return balance;
	}

	public Account(double d) {
		this.balance = d;
	}

	void deposit(double amount){
		this.balance += amount;
	}

	void withdraw(double amount){
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else {
			//throw
		}
	}
	

}
