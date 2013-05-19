package com.see.server.network;

import java.io.IOException;
import java.net.Socket;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.UUIDPair;

public interface TradingMessagerAPI {

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
