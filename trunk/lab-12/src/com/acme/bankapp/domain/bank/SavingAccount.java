package com.acme.bankapp.domain.bank;

public class SavingAccount extends AbstractAccount{
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(balance);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
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
		SavingAccount other = (SavingAccount) obj;
		if (Double.doubleToLongBits(balance) != Double
				.doubleToLongBits(other.balance))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

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
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("\tAccount type: Saving\n");
		
		result.append(super.toString());
		
		return result.toString();
	}

}
