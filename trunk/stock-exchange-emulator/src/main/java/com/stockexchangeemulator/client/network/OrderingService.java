package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private boolean isConnected = false;
	private List<ResponseObserver> observers;
	private LinkedBlockingQueue<Integer> responseOrderID;
	private Thread listenThread;
	private Runnable listenThreadRunnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Object response = null;
					response = inputStream.readObject();
					if (response instanceof Response) {
						notifyObservers((Response) response);
					} else if (response instanceof Integer)
						responseOrderID.add((int) response);
				} catch (ClassNotFoundException | IOException breakException) {
					disconnect();
					break;
				}
			}
		}
	};

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
			if (isConnected == false) {
				try {
					createConnection();
				} catch (Exception e) {
					throw new NoLoginException("Unable to create connection");
				}
			}

			outputStream.writeObject(loginName);

			readDelayedResponses(inputStream);

			listenThread = new Thread(listenThreadRunnable);
			listenThread.start();
		} catch (IOException | ClassNotFoundException | NoLoginException e) {
			throw new NoLoginException(e.getMessage());
		}
	}

	private void createConnection() throws UnknownHostException, IOException {
		socket = new Socket("localhost", DEFAULT_PORT);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
	}

	private void readDelayedResponses(ObjectInputStream inputStream)
			throws ClassNotFoundException, IOException, NoLoginException {
		LinkedList<Response> delayedResponses = new LinkedList<>();
		while (true) {
			Object messageObject = inputStream.readObject();
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
			outputStream.writeObject(order);
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
			outputStream.writeObject("disconnect");
			inputStream.close();
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			log.warning("Unable to close connection");
		}
	}
}
