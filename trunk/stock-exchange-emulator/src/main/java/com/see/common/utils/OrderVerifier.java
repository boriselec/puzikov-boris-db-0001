package com.see.common.utils;

import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.message.OrderRequest;

public class OrderVerifier {

	public void verifyTradeOrder(Order order) throws BadOrderException {

		verifyNumbers(order);
	}

	public void verifyTradeOrder(Order order, String[] tickerSymbols)
			throws BadOrderException {

		verifyStockName(order, tickerSymbols);

		verifyNumbers(order);
	}

	private void verifyNumbers(Order order) throws BadOrderException {
		if (order.getQuantity() <= 0)
			throw new BadOrderException(order.getOrderID(),
					"Order shares count should be positive");

		if (order.getPrice() <= 0
				&& order.getPrice() != Float.NEGATIVE_INFINITY)
			throw new BadOrderException(order.getOrderID(),
					"Order price should be positive");
	}

	private void verifyStockName(Order order, String[] tickerSymbols)
			throws BadOrderException {
		boolean isFound = false;
		for (String ticker : tickerSymbols)
			if (ticker.equals(order.getStock())) {
				isFound = true;
				break;
			}
		if (isFound == false)
			throw new BadOrderException(order.getOrderID(), String.format(
					"Stock exchange don't trade %s ticker", order.getStock()));
	}

	public void verifyTradeOrder(OrderRequest order) throws BadOrderException {
		verifyNumbers(order);
	}

	private void verifyNumbers(OrderRequest order) throws BadOrderException {
		if (order.getQuantity() <= 0)
			throw new BadOrderException(null,
					"Order shares count should be positive");

		if (order.getPrice() <= 0
				&& order.getPrice() != Float.NEGATIVE_INFINITY)
			throw new BadOrderException(null, "Order price should be positive");
	}
}
