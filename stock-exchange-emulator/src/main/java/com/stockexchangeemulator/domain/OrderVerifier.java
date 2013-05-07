package com.stockexchangeemulator.domain;

import com.stockexchangeemulator.client.service.exception.BadOrderException;

public class OrderVerifier {
	public Order getOrder(int clientID, String stockName, Operation operation,
			String type, String priceString, String sharesCountString)
			throws BadOrderException {

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
			price = (operation == Operation.BID) ? Float.POSITIVE_INFINITY
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
		Order result = new Order(clientID, stockName, operation, sharesCount,
				price);
		return result;
	}

	public Order getCancelOrder(String stockName, int orderID) {
		Order result = new Order(stockName, Operation.CANCEL, orderID);
		return result;
	}

	public void verifyOrder(Order order, String[] tickerSymbols)
			throws BadOrderException {
		boolean isFound = false;
		for (String ticker : tickerSymbols)
			if (ticker.equals(order.getStockName())) {
				isFound = true;
				break;
			}
		if (isFound == false)
			throw new BadOrderException(String.format(
					"Stock exchange don't trade %s ticker",
					order.getStockName()));

		if (order.getSharesCount() <= 0)
			throw new BadOrderException("Order shares count should be positive");

		if (order.getPrice() <= 0)
			throw new BadOrderException("Order price should be positive");
	}
}