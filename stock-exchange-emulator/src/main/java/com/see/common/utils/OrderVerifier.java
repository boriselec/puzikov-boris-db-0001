package com.see.common.utils;

import java.util.UUID;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.TradeOperation;
import com.see.common.domain.TradeOrder;
import com.see.common.exception.BadOrderException;

public class OrderVerifier {
	public TradeOrder getTradeOrder(String login, String stockName,
			TradeOperation operation, String type, String priceString,
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
			price = (operation == TradeOperation.BID) ? Float.POSITIVE_INFINITY
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
		TradeOrder result = new TradeOrder(login, stockName, operation,
				sharesCount, price);
		return result;
	}

	public CancelOrder getCancelOrder(String login, String stockName,
			UUID orderID) {
		CancelOrder result = new CancelOrder(login, stockName, orderID);
		return result;
	}

	public void verifyTradeOrder(TradeOrder order, String[] tickerSymbols)
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

		if (order.getPrice() <= 0
				&& order.getPrice() != Float.NEGATIVE_INFINITY)
			throw new BadOrderException("Order price should be positive");
	}
}
