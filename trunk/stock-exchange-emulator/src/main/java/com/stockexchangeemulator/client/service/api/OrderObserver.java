package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.domain.Response;

public interface OrderObserver extends Observer {
	void onFilled(Response response);
}
