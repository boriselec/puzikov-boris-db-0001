package com.see.common.pricing;

public class PriceComparator {

	public PriceComparator(float precision) {
		this.precision = precision;
	}

	private final float precision;

	public float getPrecision() {
		return precision;
	}

	public boolean match(float bid, float offer) {
		if (Math.abs(bid - offer) > precision && offer < bid)
			return true;
		else if (Math.abs(bid - offer) < precision)
			return true;
		else
			return false;
	}
}
