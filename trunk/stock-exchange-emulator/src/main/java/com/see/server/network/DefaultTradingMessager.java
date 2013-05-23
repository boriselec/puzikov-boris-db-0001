package com.see.server.network;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.message.IDPair;
import com.see.common.message.OrderMessage;
import com.see.common.message.TradeResponse;
import com.see.common.network.NetworkMessager;

public class DefaultTradingMessager implements TradingMessager {

	private HashMap<UUID, Integer> orderMap = new HashMap<>();

	public DefaultTradingMessager(NetworkMessager messager) {
		this.messager = messager;
	}

	private NetworkMessager messager;

	@Override
	public void sendOkMessage() throws IOException {
		messager.write("Ok");
	}

	@Override
	public void sendResponse(TradeResponse response) throws IOException {
		messager.write(response);
	}

	@Override
	public void sendOrderID(UUID orderID) throws IOException {
		if (orderMap.containsKey(orderID)) {
			IDPair message = new IDPair(orderMap.remove(orderID), orderID, true);
			messager.write(message);
		} else
			throw new IllegalArgumentException("Bad orderID");
	}

	@Override
	public void sendBadOrderID(UUID orderID) throws IOException {
		if (orderMap.containsKey(orderID)) {
			IDPair message = new IDPair(orderMap.remove(orderID), orderID,
					false);
			messager.write(message);
		} else
			throw new IllegalArgumentException("Bad orderID");
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
		if (message instanceof OrderMessage) {
			OrderMessage order = (OrderMessage) message;
			UUID newID = UUID.randomUUID();
			Order result = new Order(newID, order.getClientName(),
					order.getStockName(), order.getType(), order.getPrice(),
					order.getQuantity(), new Date());
			orderMap.put(newID, order.getLocalOrderID());
			return result;
		}
		if (message instanceof IDPair) {
			orderMap.put(((IDPair) message).getGlobalUuid(),
					((IDPair) message).getLocalID());
		}
		return message;
	}

	@Override
	public void connect() throws IOException {
		messager.connect();
	}

	@Override
	public void disconnect() throws IOException {
		messager.disconnect();
	}

}