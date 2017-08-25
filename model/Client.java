/*
 * Designed by Kegan Schaub & Brian Lee (Chat Server Program_CSC335 - Fall 2013)
 * User executes this file. It includes GUI design and message processing. It sends message to Server and gets from Server either.  
 */



package model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * This Client program connect one computer to a server and read commands from
 * the standard input which consists of the following three commands:
 * 
 * 1. put <name> Puts name into a collection with a the InetAddress as the key
 * Also retrieves and prints the list of all archived messages 2. get retrieves
 * and prints a list of all archived messages 3. quit remove the person from the
 * collection of this logged in
 * 
 * @author Dylan Clavell
 * @author Rick Mercer
 */
public class Client extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	private DataOutputStream outputToServer; // stream to server
	private DataInputStream inputFromServer; // stream from server

	JLabel enter_Label = new JLabel("Type Your Name");
	JTextField name_textField = new JTextField();
	JButton enter = new JButton("ENTER");
	
	JPanel main = new JPanel();
	JTextField type_Window = new JTextField("Type Your Name");
	JTextArea chat_Window = new JTextArea();
	JLabel window_Label = new JLabel("Chat Window");
	JLabel text_Label = new JLabel("Type Your Message");
	
	private String userName;
	String dialog = "";
	/*
	JPanel main = new JPanel();
	JTextField type_Window = new JTextField();
	JTextArea chat_Window = new JTextArea();
	String dialog = "";
	*/

	public static void main(String args[]) {
		Client c = new Client();
		c.setVisible(true);
		c.run();

	}

	public Client() {
		setTitle("Chat Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(415, 670);
		setLocation(480, 50);
		
		window_Label.setSize(300, 20);
		window_Label.setLocation(25, 20);
		
		chat_Window.setSize(350, 500);
		chat_Window.setLocation(25, 40);
		chat_Window.setFocusable(false);
		
		text_Label.setSize(200, 20);
		text_Label.setLocation(25, 550);
		
		type_Window.setSize(350, 30);
		type_Window.setLocation(25, 570);
		
		main.setLayout(null);
		main.setBackground(Color.LIGHT_GRAY);
		main.add(window_Label);
		main.add(chat_Window);
		main.add(text_Label);
		main.add(type_Window);
		this.add(main);
		
		
		WindowAdapter windowAdapter;
		

		type_Window.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processMessage(e.getActionCommand());
			}
		});

		windowAdapter = new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					outputToServer.writeUTF("has left");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
		};

		addWindowListener(windowAdapter);

		Socket sock = null;
		try {
			sock = new Socket(Server.HOST_NAME, Server.PORT_NUMBER);
			outputToServer = new DataOutputStream(sock.getOutputStream());
			inputFromServer = new DataInputStream(sock.getInputStream());

		} catch (Exception e) {
			System.out.println("In Client.connectToServer");
			e.printStackTrace();
		}

	}

	private void processMessage(String message) {
		try {

			outputToServer.writeUTF(message);

			type_Window.setText("");

		} catch (IOException ie) {
			System.out.println(ie);
		}
	}

	@Override
	public void run() {
		System.out.println("Console");
		try {
			while (true) {
				// Read from the user who is using this client
				String mssg = inputFromServer.readUTF() + "\n";

				chat_Window.append(mssg);

				System.out.println("+++++++++++++++ New command");
			}
		} catch (IOException e) {
			System.out.println("In Client.loop");
			e.printStackTrace();
		}
	}
}