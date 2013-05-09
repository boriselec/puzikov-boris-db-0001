package com.stockexchangeemulator.domain;

public class LastPriceMatcher implements PriceMatcher {

	@Override
	public float match(TradeOrder o1, TradeOrder o2, float lastDealPrice) {
		boolean isMarketBoth = (isMarket(o1) && isMarket(o2));
		boolean isMarketOne = (isMarket(o1) || isMarket(o2));
		if (isMarketBoth == true)
			return lastDealPrice;
		else if (isMarketOne && isMarket(o2))
			return o1.getPrice();
		else if (isMarketOne && isMarket(o1))
			return o2.getPrice();
		else
			return (o1.getDate().compareTo(o2.getDate()) >= 0) ? o1.getPrice()
					: o2.getPrice();
	}

	private boolean isMarket(TradeOrder order) {
		return (order.getPrice() == Float.POSITIVE_INFINITY || order.getPrice() == Float.NEGATIVE_INFINITY);
	}

}
