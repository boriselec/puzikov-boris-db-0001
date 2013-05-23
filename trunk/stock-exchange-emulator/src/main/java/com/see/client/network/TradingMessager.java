package com.see.client.network;

import java.io.IOException;
import java.util.List;

import com.see.common.exception.NoLoginException;
import com.see.common.message.IDPair;
import com.see.common.message.OrderMessage;
import com.see.common.message.TradeResponse;

public interface TradingMessager {

	public Object readResponse() throws IOException;

	public void connect() throws IOException;

	public void disconnect() throws IOException;

	public void sendLogin(String loginName) throws IOException;

	public void sendOrder(OrderMessage order) throws IOException;

	public List<TradeResponse> readDelayedResponses() throws NoLoginException,
			IOException;

	public void sendCancel(IDPair idPair) throws IOException;

}