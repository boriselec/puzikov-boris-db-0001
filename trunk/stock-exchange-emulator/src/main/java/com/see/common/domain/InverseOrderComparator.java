package com.see.common.domain;

import java.util.Comparator;

import com.see.common.pricing.PriceComparator;

public class InverseOrderComparator implements Comparator<TradeOrder> {

	@Override
	public int compare(TradeOrder o1, TradeOrder o2) {
		if (Math.abs(o1.getPrice() - o2.getPrice()) < PriceComparator.PRICE_PRECISION)
			if (o1.getDate().compareTo(o2.getDate()) != 0)
				return o1.getDate().compareTo(o2.getDate());
			else if (o1.getCancelingOrderID() == o2.getCancelingOrderID())
				return 0;
			else
				return 1;
		// TODO: random
		else
			return (o1.getPrice() < o2.getPrice()) ? 1 : -1;
	}

}
