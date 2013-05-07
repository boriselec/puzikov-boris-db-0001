package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;

public class FilledObserver {

	public FilledObserver(String clientID) {
		this.clientID = clientID;
	}

	private String clientID;

	public void onFilled(Response response) {
		// TODO Auto-generated method stub

	}

	public String getClientID() {
		return clientID;
	}
}
