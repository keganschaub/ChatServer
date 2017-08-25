/*
 * Designed by Kegan Schaub & Brian Lee (Chat Server Program_CSC335 - Fall 2013)
 * ClientHandler manages message processing. It is a sort of bridge between Server and Client.
 */

package model;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * The ClientHandler class allows a client to interact with the database.
 * Currently, the functionality is to allow a client to get added to the
 * collection of concurrently connected clients, to quit which removes them from
 * the database, and to also get a list of all people's names who are currently
 * connected. The user can enter these three requests for services
 * 
 * put <name>, quit, get
 * 
 * @author Dylan Clavell
 * @author Rick Mercer
 */
// TODO 06: extend Thread or implement Runnable
class ClientHandler extends Thread {
	private boolean currentMessage = false;
	private Socket socketFromServer;
	private Server server;

	private DataInputStream readFromClient;

	private String username;

	// private Scanner in;
	// private PrintWriter out;

	/**
	 * Construct an object that will run in it's own Thread so this object can
	 * communicate with that one connection
	 */

	public ClientHandler(Server server, Socket socketFromServer) {

		// TODO 07: Get a socket from the server, get streams from the socket
		this.server = server;
		this.socketFromServer = socketFromServer;

		try {
			readFromClient = new DataInputStream(
					socketFromServer.getInputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The void run() method in class Thread must be overridden in every class
	 * that extends Thread. The Server created a new instance of this class and
	 * sends it a start method, which is in Thread. The start method creates a
	 * new Thread which then calls the following run method where our domain
	 * specific logic goes.
	 */

	@Override
	public void run() {

		String messageFromClient = null;
		try {
			while (true) {

				messageFromClient = readFromClient.readUTF();

				// TODO 09: Read data from client
				// TODO 10: Parse the client's message and respond appropriately

				if (!currentMessage) {
					currentMessage = true;
					username = messageFromClient;
					messageFromClient = (username + " has joined the chat");
				} else {
					messageFromClient = (username + ": " + messageFromClient);
				}
				server.sendmessageToAllClients(messageFromClient);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}