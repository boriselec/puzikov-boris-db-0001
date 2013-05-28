package com.see.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.see.common.exception.BadOrderException;
import com.see.common.exception.CancelOrderException;
import com.see.common.exception.LoginException;
import com.see.common.message.CancelRequest;
import com.see.common.message.DisconnectRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.PlasedResponse;
import com.see.common.message.TradeResponse;
import com.see.common.network.NetworkMessager;
import com.see.common.network.ObjectStreamMessager;
import com.see.common.utils.OrderVerifier;

public class DefaultClient implements Client {
	private static Logger log = Logger.getLogger(DefaultClient.class.getName());
	private final static int DEFAULT_PORT = 2006;
	private boolean isConnected = false;
	private List<TradeListener> observers = new ArrayList<TradeListener>();
	private BlockingQueue<PlasedResponse> responses = new LinkedBlockingQueue<>(
			1);
	private ExecutorService listenThread;
	private TradingMessager messager;
	private AtomicInteger orderCount = new AtomicInteger();

	private OrderVerifier orderVerifier = new OrderVerifier();

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
					} else if (response instanceof PlasedResponse) {
						responses.add((PlasedResponse) response);
					} else {
						log.warning("Bad server response");
					}
				} catch (IOException breakException) {
					disconnect();
					break;
				}
			}
		}
	};

	public void login(String loginName) throws LoginException {
		if (isConnected)
			throw new LoginException("Already connected");
		if ("".equals(loginName))
			throw new LoginException("Empty login name");
		try {
			createConnection();

			messager.sendLogin(loginName);

			readDelayedResponses();

			listenThread = Executors.newSingleThreadExecutor();
			listenThread.submit(listenThreadRunnable);
			isConnected = true;

		} catch (IOException | LoginException e) {
			throw new LoginException(e.getMessage());
		}
	}

	private void createConnection() throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", DEFAULT_PORT);
		NetworkMessager networkMessager = new ObjectStreamMessager(socket);
		networkMessager.connect();
		messager = new DefaultTradingMessager(networkMessager);
	}

	private void readDelayedResponses() throws IOException, LoginException {
		LinkedList<TradeResponse> delayedResponses = new LinkedList<>();
		delayedResponses = (LinkedList<TradeResponse>) messager
				.readDelayedResponses();

		for (TradeResponse response : delayedResponses)
			notifyObservers(response);
	}

	public UUID sendOrder(OrderRequest order) throws BadOrderException,
			LoginException {
		if (!isConnected)
			throw new LoginException("Not connected");
		try {
			orderVerifier.verifyTradeOrder(order);

			int local = getLocalOrderID();
			order.setLocalOrderID(local);
			messager.sendOrder(order);

			PlasedResponse result = responses.take();

			if (result.isPlaced())
				return result.getGlobalUuid();
			else
				throw new BadOrderException("Unable to place order");

		} catch (IOException | InterruptedException e) {
			log.warning("Bad server response");
			throw new BadOrderException("Bad server response");
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

	public void sendDisconnect(DisconnectRequest request) {
		try {
			messager.sendDisconnect(request);
		} catch (IOException e) {
			log.warning("Unable to send disconnect");
		} finally {
			disconnect();
		}
	}

	private void disconnect() {
		listenThread.shutdownNow();
		isConnected = false;
	}

	public void cancelOrder(UUID orderID) throws CancelOrderException {
		try {
			int local = getLocalOrderID();
			messager.sendCancel(new CancelRequest(local, orderID));

			PlasedResponse response = responses.take();
			if (response.isPlaced() == false)
				throw new CancelOrderException(orderID, "Unable to cancel");
		} catch (IOException | InterruptedException e) {
			log.warning("Bad server response");
			throw new CancelOrderException(orderID, "Bad server response");
		}
	}
}