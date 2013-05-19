package com.see.server.stockexchange;

import java.io.IOException;
import java.net.Socket;

import com.see.domain.ClientResponse;
import com.see.domain.NetworkMessager;
import com.see.domain.UUIDPair;

public class TradingMessagerImpl implements TradingMessager {

	public TradingMessagerImpl(NetworkMessager messager) {
		this.messager = messager;
	}

	private NetworkMessager messager;

	@Override
	public void sendSuccessfullLoginMessage() throws IOException {
		messager.write("Ok");
	}

	@Override
	public void sendResponse(ClientResponse response) throws IOException {
		messager.write(response);
	}

	@Override
	public void sendOrderID(UUIDPair orderID) throws IOException {
		messager.write(orderID);
	}

	@Override
	public void sendBadOrderID() throws IOException {
		messager.write(null);
	}

	@Override
	public void sendError(String message) {
		try {
			messager.write(message);
		} catch (IOException ignoredException) {
		}
	}

	@Override
	public String readLogin() throws IOException {
		Object message = messager.read();
		if (message instanceof String == false)
			throw new IOException("Not a String");
		else {
			return (String) message;
		}
	}

	@Override
	public Object readOrder() throws IOException {
		return messager.read();
	}

	@Override
	public void connect(Socket socket) throws IOException {
		messager.connect(socket);
	}

	@Override
	public void disconnect() throws IOException {
		messager.disconnect();
	}

}