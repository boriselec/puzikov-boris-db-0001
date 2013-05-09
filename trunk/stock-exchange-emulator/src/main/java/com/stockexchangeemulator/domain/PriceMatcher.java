package com.stockexchangeemulator.domain;

public interface PriceMatcher {
	float match(TradeOrder o1, TradeOrder o2, float lastDealPrice);
}
