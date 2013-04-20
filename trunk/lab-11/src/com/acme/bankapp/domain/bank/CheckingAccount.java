package com.acme.bankapp.domain.bank;

public class CheckingAccount extends AbstractAccount{
	private double overdraft;

	public double getOverdraft() {
		return overdraft;
	}


	public CheckingAccount(int id, double b, double overdraft) throws NegativeArgumentException {
		super(id, b);
		if (overdraft > 0){
			this.overdraft = overdraft;
		}
		else {
			throw new NegativeArgumentException("Account initialize error: Overdraw is negative");
		}
	}


	public void withdraw(double amount) throws OverDraftLimitExceededException{
		if (this.balance >= amount){
			this.balance -= amount;
		}
		else if (this.balance + this.overdraft >= amount){
			this.balance = 0.0;
			this.overdraft = this.overdraft - (amount - this.balance);
		}
		else{
			
			String message = String.format("Withdraw error: trying to withdraw %f. Max: %f", 
					amount, this.balance + this.overdraft);
			throw new OverDraftLimitExceededException(message);
		}
		assert (this.balance + this.overdraft >= 0);
	}

	@Override
	public double maximumAmountToWithdraw() {
		return overdraft + balance;
		
	}
	

}
