package com.see.client.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.see.common.exception.NoLoginException;
import com.see.common.message.CancelRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.TradeResponse;
import com.see.common.network.NetworkMessager;

public class DefaultTradingMessager implements TradingMessager {

	public DefaultTradingMessager(NetworkMessager messager) {
		this.messager = messager;
	}

	private NetworkMessager messager;

	@Override
	public Object readResponse() throws IOException {
		return messager.read();
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
	public void sendOrder(OrderRequest order) throws IOException {
		messager.write(order);
	}

	@Override
	public List<TradeResponse> readDelayedResponses() throws NoLoginException,
			IOException {
		List<TradeResponse> result = new LinkedList<>();
		while (true) {
			Object messageObject = messager.read();
			if (messageObject instanceof String)
				if ("Ok".equals((String) messageObject))
					break;
				else
					throw new NoLoginException((String) messageObject);
			else if (messageObject instanceof TradeResponse) {
				result.add((TradeResponse) messageObject);
			} else
				throw new NoLoginException("Wrong server response");
		}
		return result;
	}

	@Override
	public void sendCancel(CancelRequest cancelRequest) throws IOException {
		messager.write(cancelRequest);
	}

	@Override
	public void connect() throws IOException {
		this.messager.connect();
	}

}