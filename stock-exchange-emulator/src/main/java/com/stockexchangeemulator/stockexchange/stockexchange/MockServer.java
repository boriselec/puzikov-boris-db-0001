package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Random;

import com.stockexchangeemulator.domain.Operation;
import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;
import com.stockexchangeemulator.domain.Status;
import com.stockexchangeemulator.domain.WrappedOrder;

public class MockServer {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	Object message;

	void run() {
		try {
			// 1. creating a server socket
			providerSocket = new ServerSocket(2005, 10);
			// 2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());
			// 3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			Random r = new Random();
			sendMessage(r.nextInt());
			// 4. The two parts communicate via the input and output streams
			do {
				try {
					message = in.readObject();
					if (message instanceof Order) {
						message = (Order) message;
						System.out.println("client>" + message);
						if (((Order) message).getType() == Operation.CANCEL) {
							sendMessage(((Order) message).getOrderID());
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendMessage(new Response(new WrappedOrder(0,
									((Order) message).getOrderID(),
									(Order) message, new Date()),
									Status.CANCELED, "ok", (float) 0.0, 0,
									new Date()));
						} else {
							int orderID = r.nextInt();
							sendMessage(orderID);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							sendMessage(new Response(new WrappedOrder(0,
									orderID, (Order) message, new Date()),
									Status.FULLY_FILLED, "ok", (float) 1.0, 1,
									new Date()));
						}
					} else if (message instanceof String) {
						if (message.equals("login"))
							sendMessage(1);
						if (message.equals("bye"))
							break;
					}
				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
				}
			} while (!message.equals("bye"));
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	void sendMessage(final Object response) {
		try {
			out.writeObject(response);
			out.flush();
			System.out.println("server>" + response);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(final String args[]) {
		MockServer server = new MockServer();
		while (true) {
			server.run();
		}
	}
}
