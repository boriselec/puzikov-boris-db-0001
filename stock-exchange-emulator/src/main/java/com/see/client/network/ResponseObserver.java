package com.see.client.network;

import com.see.common.domain.ClientResponse;

public interface ResponseObserver {
	void onResponse(ClientResponse response);

	void showError(String string);
}
