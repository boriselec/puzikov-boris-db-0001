package com.stockexchangeemulator.domain;

public class ConstantPriceMatcher implements PriceMatcher {

	private float matchingPrice;

	public ConstantPriceMatcher(float matchingPrice) {
		this.matchingPrice = matchingPrice;
	}

	@Override
	public float match(WrappedOrder o1, WrappedOrder o2, float lastDealPrice) {
		return matchingPrice;
	}

}
