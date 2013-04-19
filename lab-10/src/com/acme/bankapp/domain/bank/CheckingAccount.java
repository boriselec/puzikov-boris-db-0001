package com.acme.bankapp.domain.bank;

public class CheckingAccount extends AbstractAccount{
	private double overdraft;

	public double getOverdraft() {
		return overdraft;
	}


	public CheckingAccount(int id, double b, double overdraft) {
		super(id, b);
		if (overdraft > 0){
			this.overdraft = overdraft;
		}
		this.balance = b;
	}


	public void withdraw(double amount){
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else if (this.balance + this.overdraft >= amount){
			this.balance = 0.0;
			this.overdraft = this.overdraft - (amount - this.balance);
		}
		assert (this.balance + this.overdraft >= 0);
	}

	@Override
	public double maximumAmountToWithdraw() {
		return overdraft + balance;
		
	}
	

}
