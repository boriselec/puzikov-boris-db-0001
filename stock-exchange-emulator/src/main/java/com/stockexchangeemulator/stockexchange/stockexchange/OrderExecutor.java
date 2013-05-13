package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.util.Date;

import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.domain.CancelOrder;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.OrderVerifier;
import com.stockexchangeemulator.domain.TradeOrder;
import com.stockexchangeemulator.stockexchange.business.ServiceContainer;

public class OrderExecutor {
	private Messager messager;
	private OrderVerifier orderVerifier;
	private ServiceContainer serviceContainer;

	public OrderExecutor(Messager messager, ServiceContainer serviceContainer,
			OrderVerifier orderVerifier) {
		this.orderVerifier = orderVerifier;
		this.messager = messager;
		this.serviceContainer = serviceContainer;
	}

	public boolean execute(String login, Object message, int orderID)
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
			order.setDate(new Date());
			messager.sendOrderID(order.getCancelingOrderID());
			return true;

		}
		if (order instanceof TradeOrder) {
			try {
				orderVerifier.verifyTradeOrder((TradeOrder) order,
						serviceContainer.getTickerSymbols());
			} catch (BadOrderException e) {
				messager.sendBadOrderID();
			}
			order.setOrderID(orderID);
			order.setDate(new Date());
			messager.sendOrderID(orderID);
			serviceContainer.sendOrder(order);
			return true;
		}
		return false;
	}

}
