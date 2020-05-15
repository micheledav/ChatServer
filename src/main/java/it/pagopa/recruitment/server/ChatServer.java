package it.pagopa.recruitment.server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Create a simple chat server 
 *
 */
public class ChatServer {
	private int port;
	private Set<String> userNames = new HashSet<>();
	private Set<UserThread> userThreads = new HashSet<>();

	/**
	 * Creates a chat server, bound to the specified port
	 * 
	 * @param port  the port number
	 */
	public ChatServer(int port) {
		this.port = port;
	}

	/**
	 * Start a server socket on specified port
	 */
	public void execute() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("Chat Server is listening on port " + port);

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("New user connected");

				UserThread newUser = new UserThread(socket, this);
				userThreads.add(newUser);
				newUser.start();
			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Delivers a message from one user to others (broadcasting)
	 * 
	 * @param message the message to send 
	 * @param excludeUser the user to exclude
	 */
	void broadcast(String message, UserThread excludeUser) {
		for (UserThread aUser : userThreads) {
			if (aUser != excludeUser) {
				aUser.sendMessage(message);
			}
		}
	}

	/**
	 * Stores username of the newly connected client.
	 * 
	 * @param userName The username to store
	 */
	void addUserName(String userName) {
		userNames.add(userName);
	}

	/**
	 *  Removes the associated username and UserThread
	 * 
	 *  @param userName the username to remove
	 *  @param aUser the UserThread to remove
	 */
	void removeUser(String userName, UserThread aUser) {
		boolean removed = userNames.remove(userName);
		if (removed) {
			userThreads.remove(aUser);
			System.out.println("The user " + userName + " quitted");
		}
	}

	/**
	 * Get a collection of users connected
	 *   
	 * @return A collection of users connected
	 */
	Set<String> getUserNames() {
		return this.userNames;
	}

	/**
	 * Check if there are other users connected
	 * 
	 * @return true if there are other users connected, not count the currently connected user
	 */
	boolean hasUsers() {
		return !this.userNames.isEmpty();
	}
}