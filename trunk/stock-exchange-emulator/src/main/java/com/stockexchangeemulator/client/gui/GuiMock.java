package com.stockexchangeemulator.client.gui;

import com.stockexchangeemulator.client.clientside.OrderingImpl;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class GuiMock {
	public static void main(String[] args) {
		OrderingImpl client = new OrderingImpl(
				new com.stockexchangeemulator.client.service.api.OrderObserver() {

					public void onFilled(Response response) {
						// TODO Auto-generated catch block
					}
				});
		try {
			int myID = client.login();
		} catch (NoLoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			int orderID = client.sendOrder(new Order());
		} catch (BadOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
