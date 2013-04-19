package com.acme.bankapp.domain.bank;

public class SavingAccount extends AbstractAccount{
	private double balance;

	public SavingAccount(int id, double d) {
		super(id, d);
		this.balance = d;
	}


	public void withdraw(double amount){
		if (this.balance >= amount){
			this.balance -= amount;
		}

	}
		@Override
	public double maximumAmountToWithdraw() {
		return balance;
		
	}
	

}
