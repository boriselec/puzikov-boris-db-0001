package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import com.see.client.service.api.ServerSession;
import com.see.client.service.api.ResponseObserver;
import com.see.client.service.exception.BadOrderException;
import com.see.client.service.exception.NoLoginException;
import com.see.domain.ClientResponse;
import com.see.domain.NetworkMessager;
import com.see.domain.ObjectStreamMessager;
import com.see.domain.Order;
import com.see.domain.UUIDPair;

public class OrderingService implements ServerSession {
	private static Logger log = Logger.getLogger(OrderingService.class
			.getName());
	private final static int DEFAULT_PORT = 2006;
	private List<ResponseObserver> observers;
	private HashMap<UUID, UUID> iDMap = new HashMap<>();
	private Thread listenThread;
	private Runnable listenThreadRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Object response = null;
					response = messager.readResponse();
					if (response instanceof ClientResponse) {
						notifyObservers((ClientResponse) response);
					} else if (response instanceof UUIDPair) {
						synchronized (iDMap) {

							iDMap.notifyAll();
							iDMap.put(((UUIDPair) response).getLocalUuid(),
									((UUIDPair) response).getGlobalUuid());
						}
					} else {
						observers.get(0).showError((String) response);
					}
				} catch (IOException breakException) {
					disconnect();
					break;
				}
			}
		}
	};
	private TradingMessagerImpl messager;

	public OrderingService(ResponseObserver... observers) {
		if (observers == null)
			this.observers = new ArrayList<ResponseObserver>();
		else
			this.observers = new ArrayList<ResponseObserver>(
					Arrays.asList(observers));
		NetworkMessager networkMessager = new ObjectStreamMessager();
		this.messager = new TradingMessagerImpl(networkMessager);
	}

	public void login(String loginName) throws NoLoginException {
		if ("".equals(loginName))
			throw new NoLoginException("Empty login name");
		try {
			try {
				createConnection();
			} catch (Exception e) {
				throw new NoLoginException("Unable to create connection");
			}

			messager.sendLogin(loginName);

			readDelayedResponses();

			listenThread = new Thread(listenThreadRunnable);
			listenThread.start();
		} catch (IOException | ClassNotFoundException | NoLoginException e) {
			throw new NoLoginException(e.getMessage());
		}
	}

	private void createConnection() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", DEFAULT_PORT);
		messager.connect(socket);
	}

	private void readDelayedResponses() throws ClassNotFoundException,
			IOException, NoLoginException {
		LinkedList<ClientResponse> delayedResponses = new LinkedList<>();
		delayedResponses = messager.readDelayedResponses();

		for (ClientResponse response : delayedResponses)
			notifyObservers(response);
	}

	public UUID sendOrder(Order order) throws BadOrderException {
		try {
			UUID local = UUID.randomUUID();
			order.setLocalOrderID(local);
			messager.sendOrder(order);

			return readID(local);

		} catch (IOException e) {
			log.warning("Bad server response");
			throw new BadOrderException("Bad server response");
		}
	}

	private UUID readID(UUID local) {
		UUID result = null;
		synchronized (iDMap) {
			while (result == null) {
				try {
					iDMap.wait();
				} catch (InterruptedException ignored) {
				}
				result = iDMap.remove(local);
			}
			return result;
		}
	}

	public void addObserver(ResponseObserver observer) {
		observers.add(observer);
	}

	private void notifyObservers(ClientResponse response) {
		for (ResponseObserver observer : observers) {
			observer.onResponse(response);
		}
	}

	public void disconnect() {
		try {
			messager.disconnect();
		} catch (IOException e) {
			observers.get(0).showError("Lost connection");
			log.warning("Unable to close connection: " + e.getMessage());
		}
	}
}