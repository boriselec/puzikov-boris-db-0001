package com.acme.mock;

import java.io.Serializable;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.ParseFeedException;
import com.acme.bankapp.service.bank.BankDataLoaderService;

public class AddClentCommand implements Command, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6436806918838745285L;

	public AddClentCommand(String feedLine) {
		this.feedLine = feedLine;
	}

	private String feedLine;

	@Override
	public String execute(Bank bank) {
		BankDataLoaderService loaderService = new BankDataLoaderService();
		String outMessage;
		try {
			loaderService.loadLine(bank,
					this.getFeedLine());
			outMessage = "Ok!";
		} catch (ClientExistsException e) {
			outMessage = String.format("Error: %s",
					e.getMessage());
		} catch (NegativeArgumentException e) {
			outMessage = String.format("Error: %s",
					e.getMessage());
		} catch (ParseFeedException e) {
			outMessage = String.format("Error: %s",
					e.getMessage());
		}
		return outMessage;

	}

	public String getFeedLine() {
		return feedLine;
	}
	
	@Override
	public String toString() {
		return "add " + getFeedLine();
	}


}
