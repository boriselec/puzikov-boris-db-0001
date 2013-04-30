package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;

public interface Observer {

	void onFilled(Response response);
}
