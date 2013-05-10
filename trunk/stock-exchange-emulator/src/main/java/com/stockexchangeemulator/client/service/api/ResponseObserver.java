package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.domain.Response;

public interface ResponseObserver {
	void onResponse(Response response);
}
