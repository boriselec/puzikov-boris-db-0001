package com.see.common.pricing;

public class PriceComparator {
	public static final float PRICE_PRECISION = (float) 0.00001;

	public boolean match(float bid, float offer) {
		if (Math.abs(bid - offer) > PRICE_PRECISION && offer > bid)
			return true;
		else if (Math.abs(bid - offer) < PRICE_PRECISION)
			return true;
		else
			return false;
	}
}
