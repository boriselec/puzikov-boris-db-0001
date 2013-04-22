package com.acme.bankapp.domain.bank;

public class CheckingAccount extends AbstractAccount{
	
	private double overdraft;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		temp = Double.doubleToLongBits(overdraft);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckingAccount other = (CheckingAccount) obj;
		if (Double.doubleToLongBits(balance) != Double
				.doubleToLongBits(other.balance))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(overdraft) != Double
				.doubleToLongBits(other.overdraft))
			return false;
		return true;
	}


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
	}

	@Override
	public double maximumAmountToWithdraw() {
		return overdraft + balance;
		
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("\tAccount type: Checking\n");
		
		String overdrawMessage= String.format("\tOverdraw: %f%n", this.overdraft);
		result.append(overdrawMessage);
		
		result.append(super.toString());
		
		return result.toString();
	}
	

}
