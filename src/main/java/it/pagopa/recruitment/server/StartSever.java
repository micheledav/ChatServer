package it.pagopa.recruitment.server;

/**
 * Create a simple chat server listening on TCP port 10000
 * 
 * Press Ctrl + C to terminate the program.
 *
 */
public class StartSever {

	public static void main(String[] args) {
		final int DEFAULT_PORT = 10000;
		
		ChatServer server = new ChatServer(DEFAULT_PORT);
		server.execute();
	}

}
