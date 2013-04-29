package com.stockexchangeemulator.client.gui;

import com.stockexchangeemulator.client.clientside.OrderingImpl;
import com.stockexchangeemulator.client.service.api.ResponseListenerApi;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class GuiMock {
	public static void main(String[] args) {
		OrderingImpl client = new OrderingImpl();
		try {
			client.login();
		} catch (NoLoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			client.sendOrder(new Order(), new ResponseListenerApi() {

				private Response response;

				public void onFilled(Response response) {
					this.response = response;
					// TODO Auto-generated method stub

				}
			});
		} catch (BadOrderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
