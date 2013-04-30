package com.stockexchangeemulator.stockexchange.serverside;

import java.net.Socket;
import java.util.HashMap;


public class ClientMap {
	public ClientMap() {
		clientsListenerMap = new HashMap<String, FilledObserver>();
		clientsSocketMap = new HashMap<String, Socket>();
	}

	HashMap<String, Socket> clientsSocketMap;
	HashMap<String, FilledObserver> clientsListenerMap;
}
