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
	public static Gender getByChar(String ch) {
		if ("m".equals(ch))
			return MALE;
		else if ("f".equals(ch))
			return FEMALE;
		else 
			throw new IllegalArgumentException();
		
	}
}
