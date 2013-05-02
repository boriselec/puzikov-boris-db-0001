package com.stockexchangeemulator.domain;

import java.util.Comparator;

public class InverseOrderComparator implements Comparator<WrappedOrder> {

	@Override
	public int compare(WrappedOrder wo1, WrappedOrder wo2) {
		Order o1 = wo1.getOrder();
		Order o2 = wo2.getOrder();
		if (Math.abs(o1.getPrice() - o2.getPrice()) < PriceComparator.PRICE_PRECISION)
			if (wo1.getDate().compareTo(wo2.getDate()) != 0)
				return wo1.getDate().compareTo(wo2.getDate());
			else if (wo1.getOrderID() == wo2.getOrderID())
				return 0;
			else
				return 1;
		// TODO: random
		else
			return (o1.getPrice() < o2.getPrice()) ? 1 : -1;
	}

}
