package com.stockexchangeemulator.stockexchange.business;

import java.util.TreeSet;

import com.stockexchangeemulator.domain.Order;

public class OrderBook {
	TreeSet<Order> sellOrders;
	TreeSet<Order> buyOrders;
}
