package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import com.see.client.service.exception.NoLoginException;
import com.see.domain.ClientResponse;
import com.see.domain.NetworkMessager;
import com.see.domain.Order;

public class TradingMessagerImpl {

	public TradingMessagerImpl(NetworkMessager messager) {
		this.messager = messager;
	}

	private NetworkMessager messager;

	public Object readResponse() throws IOException {
		return messager.read();
	}

	public void connect(Socket socket) throws IOException {
		messager.connect(socket);
	}

	public void disconnect() throws IOException {
		sendDisconnect();
		messager.disconnect();
	}

	private void sendDisconnect() throws IOException {
		messager.write("disconnect");
	}

	public void sendLogin(String loginName) throws IOException {
		messager.write(loginName);
	}

	public void sendOrder(Order order) throws IOException {
		messager.write(order);
	}

	public LinkedList<ClientResponse> readDelayedResponses()
			throws NoLoginException, IOException {
		LinkedList<ClientResponse> result = new LinkedList<>();
		while (true) {
			Object messageObject = messager.read();
			if (messageObject instanceof String)
				if ("Ok".equals((String) messageObject))
					break;
				else
					throw new NoLoginException((String) messageObject);
			else if (messageObject instanceof ClientResponse) {
				result.add((ClientResponse) messageObject);
			} else
				throw new NoLoginException("Wrong server response");
		}
		return result;
	}

}