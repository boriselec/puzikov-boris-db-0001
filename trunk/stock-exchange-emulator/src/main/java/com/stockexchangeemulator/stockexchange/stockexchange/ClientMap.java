package com.stockexchangeemulator.stockexchange.stockexchange;

import java.net.Socket;
import java.util.HashMap;

import com.stockexchangeemulator.stockexchange.api.FilledObserver;

public class ClientMap {
	public ClientMap() {
		clientsListenerMap = new HashMap<Integer, FilledObserver>();
		clientsSocketMap = new HashMap<Integer, Socket>();
	}

	HashMap<Integer, Socket> clientsSocketMap;
	HashMap<Integer, FilledObserver> clientsListenerMap;
}
