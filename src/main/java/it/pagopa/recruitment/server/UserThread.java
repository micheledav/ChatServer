package it.pagopa.recruitment.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 *
 */
public class UserThread extends Thread {
	private Socket socket;
	private ChatServer server;
	private PrintWriter writer;

	public UserThread(Socket socket, ChatServer server) {
		this.socket = socket;
		this.server = server;
	}

	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));

			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
			
			printUsers();

			writer.println("Enter your name:");
			String userName = reader.readLine();
			server.addUserName(userName);
			
			String serverMessage = "New user connected: " + userName;
			server.broadcast(serverMessage, this);

			writer.println("Start chatting or write \"quit\" to leave:");
			String clientMessage;

			do {
				clientMessage = reader.readLine();
				serverMessage = "[" + userName + "]: " + clientMessage;
				server.broadcast(serverMessage, this);
				
			} while (clientMessage != null && !clientMessage.equals("quit"));

			server.removeUser(userName, this);
			socket.close();

			serverMessage = userName + " has quitted.";
			server.broadcast(serverMessage, this);

		} catch (IOException ex) {
			System.out.println("Error in UserThread: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Sends a list of online users to the newly connected user.
	 */
	void printUsers() {
		if (server.hasUsers()) {
			writer.println("Connected users: " + server.getUserNames());
		} else {
			writer.println("No other users connected");
		}
		
	}

	/**
	 * Sends a message to the client.
	 * 
	 * @param message the message to send
	 */
	void sendMessage(String message) {
		writer.println(message);
	}
}