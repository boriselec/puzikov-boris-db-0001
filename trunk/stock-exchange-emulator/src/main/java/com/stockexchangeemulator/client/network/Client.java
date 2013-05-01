package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
	private final static int defaultPort = 4000;
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void runClient() {
		if (port == 0)
			port = defaultPort;
		try (SocketChannel socketChannel = SocketChannel.open()) {
			System.out.println("Client started");
			socketChannel.configureBlocking(true);
			if (socketChannel.connect(new InetSocketAddress("localhost", port))) {
				System.out.println("Connected to server");

				ObjectInputStream ois = new ObjectInputStream(socketChannel
						.socket().getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socketChannel
						.socket().getOutputStream());

				String s = (String) ois.readObject();
				System.out.println("String is: '" + s + "'");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Client client = new Client();
		client.runClient();
	}
}
