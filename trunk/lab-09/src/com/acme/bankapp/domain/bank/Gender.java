package com.acme.bankapp.domain.bank;

public enum Gender {
	MALE,
	FEMALE;
	@Override
	public String toString() {
		String result = (this.name() == "MALE") ? "Mr. " : "Ms. ";
		return result;
	}
}
