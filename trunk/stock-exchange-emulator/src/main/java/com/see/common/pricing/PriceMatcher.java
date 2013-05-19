package com.see.common.pricing;

import com.see.common.domain.TradeOrder;

public interface PriceMatcher {
	float match(TradeOrder o1, TradeOrder o2, float lastDealPrice);
}
