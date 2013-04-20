package com.acme.bankapp.domain.bank;

public enum Gender {
	MALE("Mr. "),
	FEMALE("Ms. ");
	
	private String salut;
	private Gender(String salut){
		this.salut = salut;
	}
	@Override
	public String toString() {
		return salut;
	}
	public static Gender convertChar(char gender) {
		if (gender == 'm'){
			return MALE;
		}
		else if (gender == 'f'){
			return FEMALE;
		}
		else {
			throw new IllegalArgumentException();
		}
	}
}
