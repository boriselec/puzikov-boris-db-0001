package com.acme.bankapp.service.bank;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.CheckingAccount;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.Gender;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.ParseFeedException;
import com.acme.bankapp.domain.bank.SavingAccount;
import com.acme.domain.email.Client;

public class BankDataLoaderService {
	
	private static final String FEED_PATTERN_STRING = 
			"^accounttype=(c|s);balance=(\\d+);overdraft=(\\d+);name=(\\w+);gender=(f|m);$";

	public void loadFeed(Bank bank, LineNumberReader lnr) 
			throws IOException, ParseFeedException, ClientExistsException, NegativeArgumentException {
		
		String line = lnr.readLine();
		while (line != null) {
			loadLine(bank, line);
			line = lnr.readLine();
		}
	}
	
	public void loadLine(Bank bank, String line) 
			throws ClientExistsException, NegativeArgumentException, ParseFeedException{
		
			Pattern pattern = Pattern.compile(FEED_PATTERN_STRING);
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				
				String accountType = matcher.group(1);
				double balance = Double.parseDouble(matcher.group(2));
				double overdraw = Double.parseDouble(matcher.group(3));
				String name = matcher.group(4);
				char gender = matcher.group(5).charAt(0);
				
				if ("c".equals(accountType)){
					bank.addClient(new Client(name, Gender.convertChar(gender) , 
							new CheckingAccount(bank.getID(), balance, overdraw)));
				}
				if ("s".equals(accountType)){
					bank.addClient(new Client(name, Gender.convertChar(gender) , 
							new SavingAccount(bank.getID(), balance)));
				}
				
			} else {
				throw new ParseFeedException(String.format(
						"Parse feed error: line %s is not valid\n", line));
			}
		
	}
}
