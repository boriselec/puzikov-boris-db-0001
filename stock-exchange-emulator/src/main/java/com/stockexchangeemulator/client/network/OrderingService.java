package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.api.ResponseObserver;
import com.stockexchangeemulator.client.service.exception.BadOrderException;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class OrderingService implements OrderingApi {
	private static Logger log = Logger.getLogger(OrderingService.class
			.getName());
	private final static int DEFAULT_PORT = 2006;
	private Socket socket;
	private List<ResponseObserver> observers;
	private LinkedBlockingQueue<Integer> responseOrderID;
	private Thread listenThread;
	private Runnable listenThreadRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Object response = null;
					response = messanger.readMessage();
					if (response instanceof Response) {
						notifyObservers((Response) response);
					} else if (response instanceof Integer)
						responseOrderID.add((int) response);
					else {
						observers.get(0).showError((String) response);
					}
				} catch (IOException breakException) {
					disconnect();
					break;
				}
			}
		}
	};
	private Messager messanger;

	public OrderingService(ResponseObserver... observers) {
		if (observers == null)
			this.observers = new ArrayList<ResponseObserver>();
		else
			this.observers = new ArrayList<ResponseObserver>(
					Arrays.asList(observers));
		responseOrderID = new LinkedBlockingQueue<>();
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

			messanger.sendLogin(loginName);

			readDelayedResponses();

			listenThread = new Thread(listenThreadRunnable);
			listenThread.start();
		} catch (IOException | ClassNotFoundException | NoLoginException e) {
			throw new NoLoginException(e.getMessage());
		}
	}

	private void createConnection() throws UnknownHostException, IOException {
		socket = new Socket("localhost", DEFAULT_PORT);
		this.messanger = new Messager(socket);
	}

	private void readDelayedResponses() throws ClassNotFoundException,
			IOException, NoLoginException {
		LinkedList<Response> delayedResponses = new LinkedList<>();
		while (true) {
			Object messageObject = messanger.readMessage();
			if (messageObject instanceof String)
				if ("Ok".equals((String) messageObject))
					break;
				else
					throw new NoLoginException((String) messageObject);
			else if (messageObject instanceof Response) {
				delayedResponses.add((Response) messageObject);
			} else
				throw new NoLoginException("Wrong server response");
		}
		for (Response response : delayedResponses)
			notifyObservers(response);
	}

	public int sendOrder(Order order) throws BadOrderException {
		int result = 0;
		try {
			messanger.sendOrder(order);
			try {
				result = responseOrderID.take();
				if (result == -1)
					throw new BadOrderException();
			} catch (InterruptedException ignoredException) {

			}
		} catch (IOException e) {
			log.warning("Bad server response");
			throw new BadOrderException("Bad server response");
		}
		return result;
	}

	public void addObserver(ResponseObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(ResponseObserver observer) {
		observers.remove(observer);
	}

	private void notifyObservers(Response response) {
		for (ResponseObserver observer : observers) {
			observer.onResponse(response);
		}
	}

	public void disconnect() {
		try {
			messanger.closeStreams();
			socket.close();
		} catch (IOException e) {
			observers.get(0).showError("Lost connection");
			log.warning("Unable to close connection: " + e.getMessage());
		}
	}
}