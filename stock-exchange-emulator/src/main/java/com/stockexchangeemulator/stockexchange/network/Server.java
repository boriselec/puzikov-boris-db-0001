package com.stockexchangeemulator.stockexchange.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
	private final static int defaultPort = 4000;
	private int port;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void runServer() {
		if (port == 0)
			port = defaultPort;
		System.out.println("Sender Start");

		try (ServerSocketChannel ssChannel = ServerSocketChannel.open()) {
			ssChannel.configureBlocking(true);
			int port = 12345;
			ssChannel.socket().bind(new InetSocketAddress("localhost", port));
			ssChannel.configureBlocking(true);

			String obj = "testtext";
			while (true) {
				SocketChannel sChannel = ssChannel.accept();

				ObjectOutputStream oos = new ObjectOutputStream(sChannel
						.socket().getOutputStream());
				oos.writeObject(obj);
				oos.close();

				System.out.println("Connection ended");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Server server = new Server();
		server.runServer();
	}

}
