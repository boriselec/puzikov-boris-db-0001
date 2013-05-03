package com.stockexchangeemulator.client.gui;

import com.stockexchangeemulator.client.network.NetworkService;
import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class GuiMock {
	public static void main(String[] args) {
		NetworkService client = new NetworkService(new OrderObserver() {

			public void onResponse(Response response) {
				// TODO Auto-generated method stub

			}

		});
		try {
			int myID = client.login();
		} catch (NoLoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			int orderID = client.sendOrder(new Order("IBM", Operation.OFFER, 1,
					Float.POSITIVE_INFINITY));
		} catch (BadOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
