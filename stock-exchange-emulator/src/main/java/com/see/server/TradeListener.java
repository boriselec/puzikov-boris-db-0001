package com.see.server;

import com.see.common.domain.Trade;

public abstract class TradeListener {

	public TradeListener(String clientID) {
		this.clientName = clientID;
	}

	private String clientName;

	public abstract void onTrade(Trade trade);

	public String getClientName() {
		return clientName;
	}
}
