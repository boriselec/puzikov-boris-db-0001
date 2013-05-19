package com.see.domain;

import java.io.IOException;
import java.net.Socket;

public interface NetworkMessager {

	public void connect(Socket socket) throws IOException;

	public void write(Object message) throws IOException;

	public Object read() throws IOException;

	public void disconnect() throws IOException;

}
