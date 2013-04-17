package com.acme.bankapp.domain.bank;


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

	public Client(String name, Gender gender, AbstractAccount abstractAccount) {
		this.name = name;
		this.gender = gender;
		this.accounts = abstractAccount;
	}

	private AbstractAccount accounts;

	public AbstractAccount getAccounts() {
		return accounts;
	}

	public String getSalutation(){
		String result = this.getGender().toString() + this.getName();
		return result;
	}
	
	public String toString(){
		return new String(this.getName() + "\n" + this.getAccounts());
		
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Client == false){
			return false;
		}
		return (this.name == ((Client)obj).name);
		
	}
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

}
