package com.see.common.comparator;

import java.util.Comparator;

import com.see.common.domain.Order;
import com.see.common.pricing.PriceComparator;

public class OrderComparator implements Comparator<Order> {

	@Override
	public int compare(Order o1, Order o2) {
		if (Math.abs(o1.getPrice() - o2.getPrice()) < PriceComparator.PRICE_PRECISION)
			if (o1.getDate().compareTo(o2.getDate()) != 0)
				return o1.getDate().compareTo(o2.getDate());
			else
				return 1;
		else
			return (o1.getPrice() > o2.getPrice()) ? 1 : -1;
	}
}
