package com.see.client.network;

import com.see.common.message.TradeResponse;

public interface TradeListener {
	void onTrade(TradeResponse response);
}
