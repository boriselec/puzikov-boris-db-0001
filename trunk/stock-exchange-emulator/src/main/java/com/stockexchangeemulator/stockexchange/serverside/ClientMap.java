package com.stockexchangeemulator.stockexchange.serverside;

import java.net.Socket;
import java.util.HashMap;

import com.stockexchangeemulator.stockexchange.api.FilledListenerApi;

public class ClientMap {
	public ClientMap() {
		clientsListenerMap = new HashMap<String, FilledListenerApi>();
		clientsSocketMap = new HashMap<String, Socket>();
	}

	HashMap<String, Socket> clientsSocketMap;
	HashMap<String, FilledListenerApi> clientsListenerMap;
}
