package com.see.client.network;

import java.io.IOException;
import java.util.List;

import com.see.common.exception.NoLoginException;
import com.see.common.message.CancelRequest;
import com.see.common.message.DisconnectRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.TradeResponse;

public interface TradingMessager {

	public Object readResponse() throws IOException;

	public void connect() throws IOException;

	public void disconnect(DisconnectRequest request) throws IOException;

	public void sendLogin(String loginName) throws IOException;

	public void sendOrder(OrderRequest order) throws IOException;

	public List<TradeResponse> readDelayedResponses() throws NoLoginException,
			IOException;

	public void sendCancel(CancelRequest cancelRequest) throws IOException;

}