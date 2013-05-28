package com.see.common.comparator;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

import com.see.common.domain.Order;
import com.see.common.domain.OrderType;

public class OfferComparatorTest extends TestCase {

	private final float precision = (float) 0.001;

	@Test
	public void testShouldSortWhenSortedSetCompare() {
		Date olderDate = new Date();
		Date newerDate = new Date();
		Order[] orders = {
				new Order(UUID.randomUUID(), "0", "test", OrderType.SELL,
						Float.NEGATIVE_INFINITY, 1, newerDate),
				new Order(UUID.randomUUID(), "1", "test", OrderType.SELL, 100,
						1, olderDate),
				new Order(UUID.randomUUID(), "2", "test", OrderType.SELL, 100,
						1, newerDate),
				new Order(UUID.randomUUID(), "3", "test", OrderType.SELL, 200,
						1, newerDate) };
		Set<Order> sortedSet = new TreeSet<>(new OfferComparator(precision));
		sortedSet.addAll(Arrays.asList(orders));

		Iterator<Order> iter = sortedSet.iterator();
		for (Integer i = 0; i < orders.length; i++) {
			assertEquals(iter.next().getClientName(), i.toString());
		}
	}
}
