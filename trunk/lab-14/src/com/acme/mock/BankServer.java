package com.acme.mock;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.acme.bankapp.domain.bank.Bank;
import com.acme.bankapp.domain.bank.ClientExistsException;
import com.acme.bankapp.domain.bank.NegativeArgumentException;
import com.acme.bankapp.domain.bank.ParseFeedException;
import com.acme.bankapp.service.bank.BankDataLoaderService;
import com.acme.bankapp.service.bank.BankService;

public class BankServer {
	ServerSocket providerSocket;
	Socket connection = null;
	ObjectOutputStream out;
	ObjectInputStream in;
	Command message;
	int port;
	Bank deserializedBank = null;

	public BankServer(int port) {
		this.port = port;
	}
	
	public BankServer(Bank bank, int port) {
		this.deserializedBank = bank;
		this.port = port;
	}

	void run() {
		try {
			Bank bank = this.deserializedBank;
			BankReport report = new BankReport();
			
			// 1. creating a server socket
			providerSocket = new ServerSocket(port, 10);
			// 2. Wait for connection
			System.out.println("Waiting for connection");
			connection = providerSocket.accept();
			System.out.println("Connection received from "
					+ connection.getInetAddress().getHostName());
			// 3. get Input and Output streams
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
			sendMessage("Connection successful");
			// 4. The two parts communicate via the input and output streams
			do {
				try {
					message = (Command)in.readObject();
					
					if (message == Command.CLOSE_CONNECTION_COMMAND){
						sendMessage("bye");
						break;
					}
					
					System.out.println("client>" + message.toString());
					String outMessage = report.execute(bank, message);
					sendMessage(outMessage);
					
				} catch (ClassNotFoundException classnot) {
					System.err.println("Data received in unknown format");
				}
			} while (message != Command.CLOSE_CONNECTION_COMMAND);
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

	void sendMessage(final String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("server>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BankServer server = new BankServer(Integer.parseInt(args[0]));
		while (true) {
			server.run();
		}
	}

	public void startServer() {
		
		while (true) {
			this.run();
		}
	}
}
