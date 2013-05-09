package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;

public class FilledObserver {

	public FilledObserver(String clientID) {
		this.login = clientID;
	}

	private String login;

	public void onFilled(Response response) {
		// TODO Auto-generated method stub

	}

	public String getClientLogin() {
		return login;
	}
}
