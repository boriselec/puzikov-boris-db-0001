package com.acme.bankapp.domain.bank;

public abstract class AbstractAccount implements Account {

	protected int id;
	protected double balance;

	public AbstractAccount(final double amount) {
		this.balance = amount;
	}

	public double getBalance() {
		return balance;
	}

	public abstract void deposit(final double amount) throws NegativeArgumentException;

	public abstract void withdraw(final double amount) throws NotEnoughFundsException, NegativeArgumentException;

	public double maximumAmountToWithdraw() {
		// TODO Auto-generated method stub
		return 0.0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Client == false){
			return false;
		}
		return (this.id == ((AbstractAccount)obj).id);
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		return new String("id :" + id + "\nbalanse: " + balance + "\n");
	}
	
	public long decimalValue() {
		return Math.round(this.balance);
	}
	

}
