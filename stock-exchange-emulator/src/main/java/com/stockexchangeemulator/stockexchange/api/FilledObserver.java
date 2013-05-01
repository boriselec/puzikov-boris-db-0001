package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;

public class FilledObserver {

	public FilledObserver(int clientID) {
		this.clientID = clientID;
	}

	private int clientID;

	public void onFilled(Response response) {
		// TODO Auto-generated method stub

	}

	public int getClientID() {
		return clientID;
	}
}
