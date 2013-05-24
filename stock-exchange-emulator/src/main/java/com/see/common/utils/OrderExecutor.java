package com.see.common.utils;

import java.io.IOException;
import java.util.UUID;

import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.DisconnectException;
import com.see.common.message.IDPair;
import com.see.server.business.ServiceContainer;

public class OrderExecutor {
	private ServiceContainer serviceContainer;
	private OrderVerifier orderVerifier = new OrderVerifier();

	public OrderExecutor(ServiceContainer serviceContainer) {
		this.serviceContainer = serviceContainer;
	}

	public UUID execute(String login, Object message)
			throws DisconnectException, IOException, BadOrderException,
			CancelOrderException {
		if (message instanceof String) {
			if ("disconnect".equals((String) message))
				throw new DisconnectException();
			else
				throw new BadOrderException();
		} else if (message instanceof IDPair) {
			return executeCancel((IDPair) message);
		} else if (message instanceof Order) {
			return executeOrder((Order) message);
		} else
			throw new BadOrderException();
	}

	private UUID executeOrder(Order order) throws IOException,
			BadOrderException {
		orderVerifier.verifyTradeOrder(order,
				serviceContainer.getTickerSymbols());
		serviceContainer.placeOrder(order);
		return order.getOrderID();
	}

	private UUID executeCancel(IDPair orderIdPair) throws IOException,
			CancelOrderException {
		UUID cancelingID = orderIdPair.getGlobalUuid();
		serviceContainer.cancelOrder(cancelingID);
		return cancelingID;
	}
}
