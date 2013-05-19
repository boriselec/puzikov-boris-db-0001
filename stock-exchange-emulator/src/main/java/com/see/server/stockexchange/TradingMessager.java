package com.see.server.stockexchange;

import java.io.IOException;
import java.net.Socket;

import com.see.domain.ClientResponse;
import com.see.domain.UUIDPair;

public interface TradingMessager {

	public void connect(Socket socket) throws IOException;

	public void sendSuccessfullLoginMessage() throws IOException;

	public void sendResponse(ClientResponse response) throws IOException;

	public void sendOrderID(UUIDPair uuidPair) throws IOException;

	public void sendBadOrderID() throws IOException;

	public void sendError(String message) throws IOException;

	public String readLogin() throws IOException;

	void disconnect() throws IOException;

	public Object readOrder() throws IOException;

}
