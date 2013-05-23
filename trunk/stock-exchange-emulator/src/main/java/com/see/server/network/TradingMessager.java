package com.see.server.network;

import java.io.IOException;
import java.util.UUID;

import com.see.common.message.TradeResponse;

public interface TradingMessager {

	public void connect() throws IOException;

	public void sendOkMessage() throws IOException;

	public void sendResponse(TradeResponse response) throws IOException;

	public void sendError(String message) throws IOException;

	public String readLogin() throws IOException;

	void disconnect() throws IOException;

	public Object readOrder() throws IOException;

	void sendBadOrderID(UUID uuid) throws IOException;

	void sendOrderID(UUID orderID) throws IOException;

}
