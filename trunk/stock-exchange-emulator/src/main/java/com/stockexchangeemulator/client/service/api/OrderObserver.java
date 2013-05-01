package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.domain.Response;

public interface OrderObserver {
	void onResponse(Response response);
}
