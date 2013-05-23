package com.see.common.utils;

import java.io.IOException;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.DisconnectException;
import com.see.common.message.IDPair;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessager;

public class OrderExecutor {
	private TradingMessager messager;
	private ServiceContainer serviceContainer;
	private OrderVerifier orderVerifier = new OrderVerifier();

	public OrderExecutor(TradingMessager messager,
			ServiceContainer serviceContainer) {
		this.messager = messager;
		this.serviceContainer = serviceContainer;
	}

	public void execute(String login, Object message)
			throws DisconnectException, IOException, BadOrderException {
		if (message instanceof String) {
			if ("disconnect".equals((String) message)) {
				throw new DisconnectException();
			}
		} else if (message instanceof IDPair) {
			// cancel
			try {
				UUID cancelingID = ((IDPair) message).getGlobalUuid();
				serviceContainer.cancelOrder(cancelingID);
				messager.sendOrderID(cancelingID);
			} catch (CancelOrderException e) {
				messager.sendOrderID(UUID.fromString("0"));
			}
		} else if (message instanceof Order) {
			try {
				orderVerifier.verifyTradeOrder((Order) message,
						serviceContainer.getTickerSymbols());
			} catch (BadOrderException e) {
				messager.sendBadOrderID(((Order) message).getOrderID());
				return;
			}
			messager.sendOrderID(((Order) message).getOrderID());
			serviceContainer.sendOrder((Order) message);
		}

		else
			throw new BadOrderException();
	}
}
