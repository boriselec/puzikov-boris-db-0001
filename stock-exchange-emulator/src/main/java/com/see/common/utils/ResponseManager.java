package com.see.common.utils;

import com.see.common.domain.Trade;
import com.see.common.message.TradeResponse;

public class ResponseManager {

	public TradeResponse createResponse(Trade trade, String clientName) {
		String bidName = trade.getBid().getClientName();
		String offerName = trade.getOffer().getClientName();
		if (bidName.equals(clientName))
			return new TradeResponse(trade.getBid().getOrderID(), offerName,
					trade.getPrice(), trade.getShares());
		else
			return new TradeResponse(trade.getOffer().getOrderID(), bidName,
					trade.getPrice(), trade.getShares());
	}
}
