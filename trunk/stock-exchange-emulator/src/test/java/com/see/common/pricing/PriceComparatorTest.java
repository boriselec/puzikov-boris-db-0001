package com.see.common.pricing;

import junit.framework.TestCase;

import org.junit.Test;

public class PriceComparatorTest extends TestCase {

	PriceComparator priceComparator = new PriceComparator((float) 0.01);

	@Test
	public void testShouldMatchOrdersWhenOrdersMatching() {
		boolean result = priceComparator.match((float) 1.0, (float) 1.0);
		assertEquals(result, true);
	}

	@Test
	public void testShouldNotMatchOrdersWhenOrdersNotMatching() {
		boolean result = priceComparator.match((float) 1.0, (float) 2.0);
		assertEquals(result, false);
	}

	@Test
	public void testShouldMatchOrdersWhenDifferentPriceButUnderPrecision() {
		boolean result = priceComparator.match((float) 1.001, (float) 1.002);
		assertEquals(result, true);
	}
}
