package com.acme.mock;

import java.io.Serializable;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.service.bank.BankService;

public class PrintCommand implements Command, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3344641142761822633L;

	@Override
	public String execute(Bank bank) {
		BankService service = new BankService();
		String outMessage = service.getPrint(bank);
		return outMessage;
		
	}
	@Override
	public String toString() {
		return "print";
	}

}
