package com.stockexchangeemulator.domain;

public interface PriceMatcher {
	float match(WrappedOrder o1, WrappedOrder o2, float lastDealPrice);
}
