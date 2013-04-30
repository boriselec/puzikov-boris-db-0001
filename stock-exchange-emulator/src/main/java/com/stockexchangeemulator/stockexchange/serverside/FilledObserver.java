package com.stockexchangeemulator.stockexchange.serverside;

import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.stockexchange.api.Observer;

public class FilledObserver implements Observer {

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
