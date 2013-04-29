package com.stockexchangeemulator.client.service.api;

import com.stockexchangeemulator.domain.Response;

public interface ResponseListenerApi {
	void onFilled(Response response);
}
