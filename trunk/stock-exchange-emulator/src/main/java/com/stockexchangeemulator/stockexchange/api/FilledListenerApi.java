package com.stockexchangeemulator.stockexchange.api;

import com.stockexchangeemulator.domain.Response;

public interface FilledListenerApi {
	void onFilled(Response response);
}
