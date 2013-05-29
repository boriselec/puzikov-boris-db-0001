package com.see.common.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ObjectStreamMessager implements NetworkMessager {

	public ObjectStreamMessager(Socket socket) {
		this.socket = socket;
	}

	final private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	@Override
	public void connect() throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void write(Object message) throws IOException {
		out.writeObject(message);
		out.reset();
		// in.reset();
	}

	@Override
	public Object read() throws IOException {
		try {
			Object message = in.readObject();
			return message;
		} catch (ClassNotFoundException e) {
			throw new IOException(e.getMessage());
		}
	}

	@Override
	public void disconnect() throws IOException {
		in.close();
		out.close();
		socket.close();
	}

}
