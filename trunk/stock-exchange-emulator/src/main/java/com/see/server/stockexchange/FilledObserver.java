package com.see.server.stockexchange;

import com.see.domain.OrderBookResponse;

public abstract class FilledObserver {

	public FilledObserver(String clientID) {
		this.clientName = clientID;
	}

	private String clientName;

	public abstract void onFilled(OrderBookResponse response);

	public String getClientName() {
		return clientName;
	}
}
