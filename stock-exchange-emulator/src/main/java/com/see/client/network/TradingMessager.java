package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;
import com.see.common.exception.NoLoginException;

public interface TradingMessager {

	public Object readResponse() throws IOException;

	public void connect(Socket socket) throws IOException;

	public void disconnect() throws IOException;

	public void sendLogin(String loginName) throws IOException;

	public void sendOrder(Order order) throws IOException;

	public LinkedList<ClientResponse> readDelayedResponses()
			throws NoLoginException, IOException;

}