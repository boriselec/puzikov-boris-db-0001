package com.acme.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class BankClient {

	private final static String WELCOME = "Avaliable Commands:\n"
			+ "1) add accounttype=(c|s);balance=(\\d+);overdraft=(\\d+);name=(\\w+);gender=(f|m);\n"
			+ "2) print\n" + "3) exit\n" + "> ";
	Socket requestSocket;
	ObjectOutputStream out;
	ObjectInputStream in;
	Command command;
	String message;

	int port;

	public BankClient(int port) {
		this.port = port;
	}

	void run() {
		try {
			// 1. creating a socket to connect to the server
			requestSocket = new Socket("localhost", port);
			System.out.println(String.format(
					"Connected to localhost in port %d", port));
			// 2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			// 3: Communicating with the server
			do {
				try {
					message = (String) in.readObject();
					System.out.println("server response>" + message);
					if ("bye".equals(message))
						break;
					
					userTerminal();

				} catch (ClassNotFoundException classNot) {
					System.err.println("data received in unknown format");
				}
			} while (!message.equals("bye"));
			System.out.println("Connection closed..");
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			// 4: Closing connection
			try {
				in.close();
				out.close();
				requestSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	private void userTerminal() throws IOException {
		System.out.print(WELCOME);
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));

		boolean messageSent = false;
		while (messageSent == false) {
			String userCommand = bufferRead.readLine();
			String[] parsedCommandStrings = userCommand.split(" ");

			messageSent = parseCommand(parsedCommandStrings);
		}

	}

	private boolean parseCommand(String[] parsedCommandStrings) {
		boolean messageSent = false;
		switch (parsedCommandStrings[0]) {
		case "exit":
			command = Command.CLOSE_CONNECTION_COMMAND;
			sendMessage(command);
			messageSent = true;
			break;
		case "add":
			if (parsedCommandStrings.length < 2) {
				System.out.print("Error: Missing add argument\n" + WELCOME);
				break;
			} else {
				command = new AddClentCommand(parsedCommandStrings[1]);
				sendMessage(command);
				messageSent = true;
				break;
			}
		case "print":
			command = new PrintCommand();
			sendMessage(command);
			messageSent = true;
			break;

		default:
			System.out.print("Error: Unknown Command\n" + WELCOME);
			break;
		}
		return messageSent;

	}

	private void sendMessage(final String msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	private void sendMessage(final Command msg) {
		try {
			out.writeObject(msg);
			out.flush();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public static void main(String[] args) {
		BankClient client = new BankClient(Integer.parseInt(args[0]));
		client.run();
	}

	public void startClient() {
		this.run();
	}

}
