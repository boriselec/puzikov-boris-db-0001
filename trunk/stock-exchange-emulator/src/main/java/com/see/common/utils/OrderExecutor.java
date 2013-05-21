package com.see.common.utils;

import java.io.IOException;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.Order;
import com.see.common.domain.TradeOrder;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.DisconnectException;
import com.see.server.business.ServiceContainer;
import com.see.server.network.TradingMessager;

public class OrderExecutor {
	private TradingMessager messager;
	private OrderVerifier orderVerifier;
	private ServiceContainer serviceContainer;

	public OrderExecutor(TradingMessager messager,
			ServiceContainer serviceContainer, OrderVerifier orderVerifier) {
		this.orderVerifier = orderVerifier;
		this.messager = messager;
		this.serviceContainer = serviceContainer;
	}

	public boolean execute(String login, Object message)
			throws DisconnectException, IOException {
		if (message instanceof String) {
			if ("disconnect".equals((String) message)) {
				throw new DisconnectException();
			}
		}
		Order order = null;
		if (message instanceof Order) {
			order = (Order) message;
		}
		if (order instanceof CancelOrder) {
			messager.sendOrderID(order.getOrderID());
			serviceContainer.cancel((CancelOrder) order);
			return true;

		}
		if (order instanceof TradeOrder) {
			try {
				orderVerifier.verifyTradeOrder((TradeOrder) order,
						serviceContainer.getTickerSymbols());
			} catch (BadOrderException e) {
				messager.sendBadOrderID();
			}
			serviceContainer.sendOrder(order);
			messager.sendOrderID(order.getOrderID());
			return true;
		}
		return false;
	}
}
