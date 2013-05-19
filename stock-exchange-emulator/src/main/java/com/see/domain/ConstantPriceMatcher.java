package com.see.domain;

public class ConstantPriceMatcher implements PriceMatcher {

	private float matchingPrice;

	public ConstantPriceMatcher(float matchingPrice) {
		this.matchingPrice = matchingPrice;
	}

	@Override
	public float match(TradeOrder o1, TradeOrder o2, float lastDealPrice) {
		return matchingPrice;
	}

}
