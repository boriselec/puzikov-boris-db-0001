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

	public CheckingAccount(double b, double overdraft) throws NegativeArgumentException  {
		super(b);
		if (overdraft >= 0){
			this.overdraft = overdraft;
		}
		else{
			throw new NegativeArgumentException("Negative overdraw");
		}
		if (b >= 0) {
			this.balance = b;
		}
		else {
			throw new NegativeArgumentException("Negative balance");
		}
	}

	public void deposit(double amount){
		this.balance += amount;
	}

	public void withdraw(double amount) throws NotEnoughFundsException, NegativeArgumentException{
		if (amount < 0){
			throw new NegativeArgumentException("Negative amount");
		}
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
	
	@Override
		public String toString() {
			return new String(super.toString() + "overdraft: " + this.overdraft + "\n");
		}
	

}
