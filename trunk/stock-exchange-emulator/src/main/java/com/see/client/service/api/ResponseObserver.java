package com.see.client.service.api;

import com.see.domain.ClientResponse;

public interface ResponseObserver {
	void onResponse(ClientResponse response);

	void showError(String string);
}
