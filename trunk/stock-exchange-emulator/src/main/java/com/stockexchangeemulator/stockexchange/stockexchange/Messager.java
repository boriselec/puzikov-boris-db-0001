package com.stockexchangeemulator.stockexchange.stockexchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.stockexchangeemulator.domain.Response;

public class Messager {
	private ObjectOutputStream out;
	private ObjectInputStream in;

	public Messager(Socket socket) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void closeStreams() throws IOException {
		in.close();
		out.close();
	}

	private void sendMessage(final Object response) throws IOException {
		out.writeObject(response);
		out.flush();
	}

	public void sendSuccessfullLoginMessage() throws IOException {
		sendMessage("Ok");
	}

	public void sendResponse(Response response) throws IOException {
		sendMessage(response);
	}

	public void sendOrderID(int cancelingOrderID) throws IOException {
		sendMessage(cancelingOrderID);
	}

	public void sendBadOrderID() throws IOException {
		sendMessage(-1);
	}

	public void sendError(String message) {
		try {
			sendMessage(message);
		} catch (IOException ignoredException) {
		}
	}

	public Object readMessage() throws IOException {
		try {
			Object message = in.readObject();
			return message;
		} catch (ClassNotFoundException ignoredException) {
		}
		return null;
	}

	public String readLogin() throws IOException {
		Object message = readMessage();
		if (message instanceof String == false)
			throw new IOException();
		else {
			return (String) message;
		}
	}

}