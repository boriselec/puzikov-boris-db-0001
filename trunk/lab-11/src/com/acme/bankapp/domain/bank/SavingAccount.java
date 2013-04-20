package com.acme.bankapp.domain.bank;

public class SavingAccount extends AbstractAccount{

	public SavingAccount(int id, double d) throws NegativeArgumentException {
		super(id, d);
	}


	public void withdraw(double amount) throws NotEnoughFundsException{
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else {
			String message = String.format("Withdraw error: trying to withdraw %f. Max: %f", 
					amount, this.balance);
			throw new NotEnoughFundsException(message);
		}

	}
		@Override
	public double maximumAmountToWithdraw() {
		return balance;
		
	}
	

}
