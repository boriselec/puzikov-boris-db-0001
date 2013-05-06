package com.stockexchangeemulator.domain;

public class LastPriceMatcher implements PriceMatcher {

	@Override
	public float match(WrappedOrder o1, WrappedOrder o2, float lastDealPrice) {
		boolean isMarketBoth = (isMarket(o1) && isMarket(o2));
		if (isMarketBoth == true)
			return lastDealPrice;
		else
			return (o1.getDate().compareTo(o2.getDate()) >= 0) ? o1.getOrder()
					.getPrice() : o2.getOrder().getPrice();
	}

	private boolean isMarket(WrappedOrder order) {
		return (order.getOrder().getPrice() == Float.POSITIVE_INFINITY || order
				.getOrder().getPrice() == Float.NEGATIVE_INFINITY);
	}

}
