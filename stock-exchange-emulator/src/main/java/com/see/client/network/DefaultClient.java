package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.see.common.domain.CancelOrder;
import com.see.common.domain.ClientResponse;
import com.see.common.domain.IDPair;
import com.see.common.domain.Order;
import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.NoLoginException;
import com.see.common.network.NetworkMessager;
import com.see.common.network.ObjectStreamMessager;
import com.see.common.utils.OrderVerifier;

public class DefaultClient implements Client {
	private static Logger log = Logger.getLogger(DefaultClient.class.getName());
	private final static int DEFAULT_PORT = 2006;
	private List<ResponseObserver> observers;
	private HashMap<Integer, UUID> iDMap = new HashMap<>();
	private Thread listenThread;
	private AtomicInteger orderCount = new AtomicInteger();

	private int getLocalOrderID() {
		return orderCount.incrementAndGet();
	}

	private Runnable listenThreadRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Object response = null;
					response = messager.readResponse();
					if (response instanceof ClientResponse) {
						notifyObservers((ClientResponse) response);
					} else if (response instanceof IDPair) {
						synchronized (iDMap) {

							iDMap.put(((IDPair) response).getLocalUuid(),
									((IDPair) response).getGlobalUuid());
							iDMap.notifyAll();
						}
					} else {
					}
				} catch (IOException breakException) {
					disconnect();
					break;
				}
			}
		}
	};
	private TradingMessager messager;

	public DefaultClient() {
		this.observers = new ArrayList<ResponseObserver>();
		NetworkMessager networkMessager = new ObjectStreamMessager();
		this.messager = new DefaultTradingMessager(networkMessager);
	}

	public void login(String loginName) throws NoLoginException {
		if ("".equals(loginName))
			throw new NoLoginException("Empty login name");
		try {
			createConnection();

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
			int local = getLocalOrderID();
			order.setLocalOrderID(local);
			messager.sendOrder(order);

			return readID(local);

		} catch (IOException e) {
			log.warning("Bad server response");
			throw new BadOrderException("Bad server response");
		}
	}

	private UUID readID(int local) {
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
		}
	}

	@Override
	public void cancelOrder(Order order) throws CancelOrderException {

		CancelOrder cancelOrder = new OrderVerifier().getCancelOrder(
				order.getLogin(), order.getStockName(), order.getOrderID());
		try {
			messager.sendOrder(cancelOrder);
		} catch (IOException e) {
			throw new CancelOrderException("Bad server response");
		}
	}
}