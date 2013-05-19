package com.see.server.network;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

import com.see.common.domain.ClientResponse;
import com.see.common.domain.Order;
import com.see.common.domain.UUIDPair;
import com.see.common.network.NetworkMessager;

public class TradingMessager implements TradingMessagerAPI {

	private HashMap<UUID, UUID> orderMap = new HashMap<>();

	public TradingMessager(NetworkMessager messager) {
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
	public void sendOrderID(UUID orderID) throws IOException {
		if (orderMap.containsKey(orderID)) {
			UUIDPair message = new UUIDPair(orderMap.remove(orderID), orderID);
			messager.write(message);
		} else
			throw new IllegalArgumentException("Bad orderID");
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
		Object message = messager.read();
		if (message instanceof Order)
			orderMap.put(((Order) message).getOrderID(),
					((Order) message).getLocalOrderID());
		return message;
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