package com.acme.bankapp.domain.bank;

public class SavingAccount extends AbstractAccount{
	private double balance;

	public double getBalance() {
		return balance;
	}

	public SavingAccount(double d)  {
		super(d);
		if (d >= 0){
		this.balance = d;
		}
		else{
			throw new IllegalArgumentException("Negative balance");
		}
	}

	public void deposit(double amount) throws NegativeArgumentException{
		if (amount < 0){
			throw new NegativeArgumentException("Negative amount");
		}
		this.balance += amount;
	}

	public void withdraw(double amount) throws NotEnoughFundsException{
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else {
			throw new NotEnoughFundsException("Not enough money to withdraw", amount);
		}

	}
		@Override
	public double maximumAmountToWithdraw() {
		return balance;
		
	}
	

}
