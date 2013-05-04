package com.stockexchangeemulator.domain;

public class LastPriceMatcher implements PriceMatcher {

	@Override
	public float match(WrappedOrder o1, WrappedOrder o2) {
		// TODO: market orders match
		return (o1.getDate().compareTo(o2.getDate()) >= 0) ? o1.getOrder()
				.getPrice() : o2.getOrder().getPrice();
	}

}
