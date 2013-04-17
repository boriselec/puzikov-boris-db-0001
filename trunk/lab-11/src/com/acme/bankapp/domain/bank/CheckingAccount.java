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

	public CheckingAccount(double b, double overdraft)  {
		super(b);
		if (overdraft >= 0){
			this.overdraft = overdraft;
		}
		else{
			throw new IllegalArgumentException("Negative overdraw");
		}
		if (b >= 0) {
			this.balance = b;
		}
		else {
			throw new IllegalArgumentException("Negative balance");
		}
	}

	public void deposit(double amount){
		this.balance += amount;
	}

	public void withdraw(double amount) throws NotEnoughFundsException{
		if (this.balance + this.overdraft>= amount){
			this.balance -= amount;
		}
		else {
			throw new OverDraftLimitExceededException("Not enough money to withdraw", amount,
					this.balance + this.overdraft);
		}
	}

	@Override
	public double maximumAmountToWithdraw() {
		return overdraft + balance;
		
	}
	

}
