package com.see.common.network;

import java.io.IOException;

public interface NetworkMessager {

	public void connect() throws IOException;

	public void write(Object message) throws IOException;

	public Object read() throws IOException;

	public void disconnect() throws IOException;

}
