package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.stockexchangeemulator.client.service.api.OrderObserver;
import com.stockexchangeemulator.client.service.api.OrderingApi;
import com.stockexchangeemulator.client.service.exception.NoLoginException;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class OrderingService implements OrderingApi {
	private final static int DEFAULT_PORT = 2006;
	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private List<OrderObserver> observers;
	private LinkedBlockingQueue<Integer> responseOrderID;

	public OrderingService(OrderObserver... observers) {
		if (observers == null)
			this.observers = new ArrayList<OrderObserver>();
		else
			this.observers = new ArrayList<OrderObserver>(
					Arrays.asList(observers));
		responseOrderID = new LinkedBlockingQueue<>();
	}

	public int login() throws NoLoginException {
		try {
			socket = new Socket("localhost", DEFAULT_PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());

			int responseClientID = (int) inputStream.readObject();
			runRead();
			return responseClientID;

		} catch (ClassNotFoundException | IOException e) {
			throw new NoLoginException(e.getMessage());
		}
	}

	private void runRead() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Object response = inputStream.readObject();
						if (response instanceof Response) {
							notifyObservers((Response) response);
						} else if (response instanceof Integer)
							responseOrderID.add((int) response);
					} catch (ClassNotFoundException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public int sendOrder(Order order) {
		int result = 0;
		try {
			outputStream.writeObject(order);
			try {
				result = responseOrderID.take();
			} catch (InterruptedException ignoredException) {

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public void addObserver(OrderObserver observer) {
		observers.add(observer);
	}

	public void removeObserver(OrderObserver observer) {
		observers.remove(observer);
	}

	public void notifyObservers(Response response) {
		for (OrderObserver observer : observers) {
			observer.onResponse(response);
		}
	}
}
