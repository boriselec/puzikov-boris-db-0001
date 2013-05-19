package com.see.domain;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ObjectStreamMessager implements NetworkMessager {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;

	@Override
	public void connect(Socket socket) throws IOException {
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	@Override
	public void write(Object message) throws IOException {
		out.writeObject(message);
	}

	@Override
	public Object read() throws IOException {
		try {
			return in.readObject();
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
