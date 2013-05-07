package com.stockexchangeemulator.domain;

public interface PriceMatcher {
	float match(Order o1, Order o2, float lastDealPrice);
}
