package com.acme.bankapp.domain.bank;

public class CheckingAccount extends AbstractAccount{
//	private double balance;
	private double overdraft;

	public double getOverdraft() {
		return overdraft;
	}

	public double getBalance() {
		return balance;
	}

	public CheckingAccount(double b, double overdraft) {
		super(b);
		if (overdraft <= 0){
			//throw
		}
		else{
			this.overdraft = overdraft;
		}
		this.balance = b;
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
		assert (this.balance + this.overdraft >= 0);
	}

	@Override
	public double maximumAmountToWithdraw() {
		return overdraft + balance;
		
	}
	

}
