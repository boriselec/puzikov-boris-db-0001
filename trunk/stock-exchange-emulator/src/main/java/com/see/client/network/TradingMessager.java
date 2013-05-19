package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;
import com.see.common.exception.NoLoginException;
import com.see.common.network.NetworkMessager;

public class TradingMessager implements TradingMessagerAPI {

	public TradingMessager(NetworkMessager messager) {
		this.messager = messager;
	}

	private NetworkMessager messager;

	@Override
	public Object readResponse() throws IOException {
		return messager.read();
	}

	@Override
	public void connect(Socket socket) throws IOException {
		messager.connect(socket);
	}

	@Override
	public void disconnect() throws IOException {
		sendDisconnect();
		messager.disconnect();
	}

	private void sendDisconnect() throws IOException {
		messager.write("disconnect");
	}

	@Override
	public void sendLogin(String loginName) throws IOException {
		messager.write(loginName);
	}

	@Override
	public void sendOrder(Order order) throws IOException {
		messager.write(order);
	}

	@Override
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