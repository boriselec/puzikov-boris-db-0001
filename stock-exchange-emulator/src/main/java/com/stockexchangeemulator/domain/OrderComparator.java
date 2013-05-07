package com.stockexchangeemulator.domain;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		if (Math.abs(o1.getPrice() - o2.getPrice()) < PriceComparator.PRICE_PRECISION)
			if (o1.getDate().compareTo(o2.getDate()) != 0)
				return o1.getDate().compareTo(o2.getDate());
			else if (o1.getOrderID() == o2.getOrderID())
				return 0;
			else
				return 1;
		// TODO: random
		else
			return (o1.getPrice() > o2.getPrice()) ? 1 : -1;
	}
}
