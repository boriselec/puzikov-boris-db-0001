package com.see.common.comparator;

import java.util.Comparator;

import com.see.common.domain.Order;

public class BidComparator implements Comparator<Order> {

	public BidComparator(float precision) {
		this.precision = precision;
	}

	float precision;

	@Override
	public int compare(Order o1, Order o2) {
		if (o1.getOrderID().equals(o2.getOrderID()))
			return 0;
		if (Math.abs(o1.getPrice() - o2.getPrice()) < precision)
			if (o1.getDate().compareTo(o2.getDate()) != 0)
				return o1.getDate().compareTo(o2.getDate());
			else
				return 1;
		else
			return (o1.getPrice() < o2.getPrice()) ? 1 : -1;
	}

}
