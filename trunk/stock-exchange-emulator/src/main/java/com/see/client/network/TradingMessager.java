package com.see.client.network;

import java.io.IOException;
import java.util.List;

import com.see.common.exception.LoginException;
import com.see.common.message.CancelRequest;
import com.see.common.message.DisconnectRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.TradeResponse;

public interface TradingMessager {

	public Object readResponse() throws IOException;

	public void connect() throws IOException;

	public void disconnect() throws IOException;

	public void sendLogin(String loginName) throws IOException;

	public void sendOrder(OrderRequest order) throws IOException;

	public List<TradeResponse> readDelayedResponses() throws LoginException,
			IOException;

	public void sendCancel(CancelRequest cancelRequest) throws IOException;

	void sendDisconnect(DisconnectRequest request) throws IOException;

}