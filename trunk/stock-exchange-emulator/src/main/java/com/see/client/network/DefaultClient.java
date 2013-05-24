package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.NoLoginException;
import com.see.common.message.IDPair;
import com.see.common.message.OrderMessage;
import com.see.common.message.TradeResponse;
import com.see.common.network.NetworkMessager;
import com.see.common.network.ObjectStreamMessager;

public class DefaultClient implements Client {
	private static Logger log = Logger.getLogger(DefaultClient.class.getName());
	private final static int DEFAULT_PORT = 2006;
	private List<TradeListener> observers;
	private HashMap<Integer, IDPair> iDMap = new HashMap<>();
	private ExecutorService listenThread;
	private TradingMessager messager;
	private AtomicInteger orderCount = new AtomicInteger();

	private int getLocalOrderID() {
		return orderCount.incrementAndGet();
	}

	private Runnable listenThreadRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Object response = messager.readResponse();
					if (response instanceof TradeResponse) {
						notifyObservers((TradeResponse) response);
					} else if (response instanceof IDPair) {
						synchronized (iDMap) {

							iDMap.put(((IDPair) response).getLocalID(),
									(IDPair) response);
							iDMap.notifyAll();
						}
					} else {
					}
				} catch (IOException breakException) {
					breakException.printStackTrace();
					disconnect();
					break;
				}
			}
		}
	};

	public DefaultClient() {
		this.observers = new ArrayList<TradeListener>();
	}

	public void login(String loginName) throws NoLoginException {
		if ("".equals(loginName))
			throw new NoLoginException("Empty login name");
		try {
			createConnection();

			messager.sendLogin(loginName);

			readDelayedResponses();

			listenThread = Executors.newSingleThreadExecutor();
			listenThread.submit(listenThreadRunnable);

		} catch (IOException | ClassNotFoundException | NoLoginException e) {
			throw new NoLoginException(e.getMessage());
		}
	}

	private void createConnection() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", DEFAULT_PORT);
		NetworkMessager networkMessager = new ObjectStreamMessager(socket);
		networkMessager.connect();
		this.messager = new DefaultTradingMessager(networkMessager);
	}

	private void readDelayedResponses() throws ClassNotFoundException,
			IOException, NoLoginException {
		LinkedList<TradeResponse> delayedResponses = new LinkedList<>();
		delayedResponses = (LinkedList<TradeResponse>) messager
				.readDelayedResponses();

		for (TradeResponse response : delayedResponses)
			notifyObservers(response);
	}

	public UUID sendOrder(OrderMessage order) throws BadOrderException,
			NoLoginException {
		try {
			int local = getLocalOrderID();
			order.setLocalOrderID(local);
			messager.sendOrder(order);

			IDPair result = readID(local);
			if (result.isPlaced())
				return result.getGlobalUuid();
			else
				throw new BadOrderException();

		} catch (IOException e) {
			log.warning("Bad server response");
			throw new BadOrderException("Bad server response");
		}
	}

	private IDPair readID(int local) {
		IDPair result = null;
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

	public void addObserver(TradeListener observer) {
		observers.add(observer);
	}

	private void notifyObservers(TradeResponse response) {
		for (TradeListener observer : observers) {
			observer.onTrade(response);
		}
	}

	public void disconnect() {
		try {
			messager.disconnect();
			listenThread.shutdownNow();
		} catch (IOException e) {
		}
	}

	public void cancelOrder(UUID orderID) throws CancelOrderException {
		try {
			int local = getLocalOrderID();
			messager.sendCancel(new IDPair(local, orderID));

			UUID canceledID = readID(local).getGlobalUuid();
			System.out.println(canceledID);
			if (canceledID.equals(orderID) == false)
				throw new CancelOrderException("Unable to cancel");
		} catch (IOException e) {
			log.warning("Bad server response");
			throw new CancelOrderException("Bad server response");
		}
	}
}