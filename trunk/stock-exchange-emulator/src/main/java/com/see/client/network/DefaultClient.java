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
import com.see.common.exception.NoLoginException;
import com.see.common.message.CancelRequest;
import com.see.common.message.OrderRequest;
import com.see.common.message.PlasedResponse;
import com.see.common.message.TradeResponse;
import com.see.common.network.NetworkMessager;
import com.see.common.network.ObjectStreamMessager;
import com.see.common.utils.OrderVerifier;

public class DefaultClient implements Client {
	private static Logger log = Logger.getLogger(DefaultClient.class.getName());
	private final static int DEFAULT_PORT = 2006;
	private List<TradeListener> observers;
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

	public UUID sendOrder(OrderRequest order) throws BadOrderException,
			NoLoginException {
		try {
			orderVerifier.verifyTradeOrder(order);

			int local = getLocalOrderID();
			order.setLocalOrderID(local);
			messager.sendOrder(order);

			PlasedResponse result = responses.take();

			if (result.isPlaced())
				return result.getGlobalUuid();
			else
				throw new BadOrderException();

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