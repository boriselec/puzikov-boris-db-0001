package com.acme.bankapp.service.bank;
import com.acme.bankapp.domain.bank.*;

public class BankApplication {
	public static void main(String[] args) {
		BankService service = new BankService();
		Bank bank = new Bank();
		service.printBalance(bank);
		
		service.addClient(bank, new Client("Bob", Gender.MALE, new CheckingAccount(2.0, 3.0)));
		service.printBalance(bank);
		
		service.addClient(bank, new Client("Alice", Gender.FEMALE, new SavingAccount(4.0)));
		service.printBalance(bank);
		
		service.modifyBank(bank);
		service.printBalance(bank);
		
		service.printMaximumAmountToWithdraw(bank);
		
	}

}
