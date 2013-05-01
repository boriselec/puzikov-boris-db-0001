package com.stockexchangeemulator.domain;

import java.util.Comparator;

public class InverseOrderComparator implements Comparator<WrappedOrder> {

	@Override
	public int compare(WrappedOrder wo1, WrappedOrder wo2) {
		Order o1 = wo1.getOrder();
		Order o2 = wo2.getOrder();
		if (Math.abs(o1.getPrice() - o2.getPrice()) < PricePrecision.PRICE_PRECISION)
			return wo2.getDate().compareTo(wo1.getDate());
		else
			return (o1.getPrice() < o2.getPrice()) ? 1 : -1;
	}

}
