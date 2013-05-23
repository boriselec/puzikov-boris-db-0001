package com.see.common.utils;

import com.see.common.domain.Order;
import com.see.common.domain.OrderType;
import com.see.common.exception.BadOrderException;
import com.see.common.message.OrderMessage;

public class OrderVerifier {
	public OrderMessage getTradeOrder(String login, String stockName,
			OrderType operation, String type, String priceString,
			String sharesCountString) throws BadOrderException {

		if ("".equals(stockName))
			throw new BadOrderException("Wrong stock name");

		float price;
		if ("limit".equals(type)) {
			try {
				price = Float.parseFloat(priceString);
			} catch (NumberFormatException e) {
				throw new BadOrderException("Cant parse price");
			}
			if (price <= 0)
				throw new BadOrderException(
						"Limit order should hava positive price");
		} else {
			price = (operation == OrderType.BUY) ? Float.POSITIVE_INFINITY
					: Float.NEGATIVE_INFINITY;
		}

		int sharesCount;
		try {
			sharesCount = Integer.parseInt(sharesCountString);
		} catch (NumberFormatException e) {
			throw new BadOrderException("Cant parse quantity");
		}
		if (sharesCount <= 0)
			throw new BadOrderException("Order should hava positive quantity");
		OrderMessage result = new OrderMessage(login, stockName, price,
				sharesCount, operation);
		return result;
	}

	public void verifyTradeOrder(Order message, String[] tickerSymbols)
			throws BadOrderException {
		boolean isFound = false;
		for (String ticker : tickerSymbols)
			if (ticker.equals(message.getStock())) {
				isFound = true;
				break;
			}
		if (isFound == false)
			throw new BadOrderException(String.format(
					"Stock exchange don't trade %s ticker", message.getStock()));

		if (message.getQuantity() <= 0)
			throw new BadOrderException("Order shares count should be positive");

		if (message.getPrice() <= 0
				&& message.getPrice() != Float.NEGATIVE_INFINITY)
			throw new BadOrderException("Order price should be positive");
	}
}
