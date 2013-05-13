package com.stockexchangeemulator.domain;

public class PriceComparator {
	public static final float PRICE_PRECISION = (float) 0.00001;

	public static boolean match(float offer, float bid) {
		if (Math.abs(offer - bid) < PRICE_PRECISION && bid > offer)
			return true;
		else if (Math.abs(offer - bid) < PRICE_PRECISION)
			return true;
		else
			return false;
	}
}
