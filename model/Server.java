/*
 * Designed by Kegan Schaub & Brian Lee (Chat Server Program_CSC335 - Fall 2013)
 * Server collects all messages from Client(s) and sends it back to Client(s).
 */

package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * This server is a thread that accepts requests to connect from clients. Each
 * request cause a new Thread to handle the client, or in other words there is
 * one thread per client on the server side.
 * 
 * Each client running in it's own thread shares the same database which is why
 * the collection class has synchronized methods to allow one request to
 * complete before another thread can jump into the middle. Use Vector<E>
 * 
 * @author Dylan Clavell
 * @author Rick Mercer
 */
public class Server implements Runnable {

	public static final int PORT_NUMBER = 4009;
	public static final String HOST_NAME = "localhost";
	private static ServerSocket myServerSocket; // client request source
	private static Socket clientSocket;
	private static HashMap<Thread, DataOutputStream> messageList;

	public static void main(String args[]) {

		try {
			Server server = new Server(PORT_NUMBER);
			System.out.println("Server connected...");
			server.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO 02: You don't need to Extend thread, but loop somehow
	}

	public Server(int port) {
		messageList = new HashMap<Thread, DataOutputStream>();
		try {
			myServerSocket = new ServerSocket(port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// This run() method is now in a separate Thread and will listen
	// for connections at PORT_NUMBER at the machine in which it is running
	@Override
	public void run() {
		while (true) {
			try {
				clientSocket = myServerSocket.accept();
				System.out.println(clientSocket);
			} catch (Exception e) {
				e.printStackTrace();
			}
			ClientHandler handleIt = new ClientHandler(this, clientSocket);
			Thread t = new Thread(handleIt);
			try {
				messageList.put(t,
						new DataOutputStream(clientSocket.getOutputStream()));
				t.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendmessageToAllClients(String message) throws IOException {
		for (DataOutputStream writeToClient : messageList.values())
			try {
				writeToClient.writeUTF(message);
				writeToClient.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}

}