package com.acme.domain.email;

import java.io.Serializable;

import com.acme.bankapp.domain.bank.AbstractAccount;
import com.acme.bankapp.domain.bank.Gender;


public class Client implements Serializable{
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accounts == null) ? 0 : accounts.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Client other = (Client) obj;
		if (accounts == null) {
			if (other.accounts != null)
				return false;
		} else if (!accounts.equals(other.accounts))
			return false;
		if (gender != other.gender)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

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
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(getSalutation() + '\n');
		result.append(accounts.toString());
		return result.toString();
	}

}
