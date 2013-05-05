package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class MockClient {

	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	Object message;

	Response run(Order order) {
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket("localhost", 2004);
			System.out.println("Connected to localhost in port 2004");
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			// 3: Communicating with the server
			do {
				try {
					message = in.readObject();
					if (message instanceof Response)
						return (Response) message;
					System.out.println("server>" + message);
					sendMessage(order);
				} catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
			} while (true);
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
		return null;
	}

	void sendMessage(final Order order) {
		try {
			out.writeObject(order);
			out.flush();
			System.out.println("client>" + order.toString());
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(final String args[]) {
		MockClient client = new MockClient();
		client.run(new Order("qw", Operation.OFFER, 0));
	}

}
