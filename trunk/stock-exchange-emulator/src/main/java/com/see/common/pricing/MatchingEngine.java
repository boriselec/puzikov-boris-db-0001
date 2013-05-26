package com.see.common.pricing;

import com.see.common.domain.Order;

public interface MatchingEngine {
	/**
	 * 
	 * pair of orders
	 * 
	 * @param o1
	 * @param o2
	 * 
	 * @param lastDealPrice
	 *            price of last deal in current orderbook
	 * @return deal price for o1 and o2
	 */
	float getPrice(Order o1, Order o2, float lastDealPrice);

	boolean isMatch(Order bid, Order offer);
}
