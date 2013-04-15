package com.acme.bank;

public class Client {
	
	private Gender gender;
	
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Client(String name, Gender gender, Account accounts) {
		this.name = name;
		this.gender = gender;
		this.accounts = accounts;
	}

	private Account accounts;

	public Account getAccounts() {
		return accounts;
	}

	public String getSalutation(){
		String result = this.getGender().toString() + this.getName();
		return result;
	}

}
