package com.stockexchangeemulator.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.stockexchangeemulator.domain.Order;
import com.stockexchangeemulator.domain.Response;

public class Messager {
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Messager(Socket socket) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void closeStreams() throws IOException {
		out.writeObject("disconnect");
		in.close();
		out.close();
	}

	private void sendMessage(final Object response) throws IOException {
		out.writeObject(response);
		out.flush();
	}

	public Object readMessage() throws IOException {
		try {
			Object message = in.readObject();
			return message;
		} catch (ClassNotFoundException ignoredException) {
		}
		return null;
	}

	public void sendLogin(String loginName) throws IOException {
		sendMessage(loginName);
	}

	public void sendOrder(Order order) throws IOException {
		sendMessage(order);
	}

	public Response readResponse() throws IOException {
		Object message = readMessage();
		if (message instanceof Response == false)
			throw new IOException();
		else {
			return (Response) message;
		}
	}

}
