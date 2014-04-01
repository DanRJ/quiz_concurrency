package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 
 * @author Daniel Rustad Johansen
 *
 */
public class Client {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket serverContact;
	
	public Client() {
		Scanner in = new Scanner(System.in);
		String message = "";
		System.out.println("Forsøker oppkobling");
		
		try {
			serverContact = new Socket("84.208.113.186", 8000);
			output = new ObjectOutputStream(serverContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(serverContact.getInputStream());
			
			System.out.println("Koblet til");
			while(true) {
				message = readMessagesFromServer(message);
				
				if (message.toString().contains("slutt")) {
					String[] test = message.split("slutt");
					System.out.println(test[0]);
					break;
				}
				System.out.println(message);
				sendData(in.nextLine());
			}
		} catch (EOFException eofException) {
			System.out.println("Server lukket forbindelsen\n");
		} catch (IOException ioException) {
			System.out.println("Problem med server-forbindelsen:"+ioException.getMessage());
		} finally {
			exit();
		}
	}
	
	/**
	 * Stops the program by closing the Socket and Input/Output streams
	 */
	private void exit() {
		sendData("slutt");
		try {
			serverContact.close();
			output.close();
			input.close();
		} catch (IOException ioException) {
			System.out.println("Something went wrong" + ioException.getMessage());
		}
	}
	
	/**
	 * Fetches messages written from the server
	 * 
	 * @param message		a String, the message sent to/from server
	 * @return				a String, the message the server sent to the client
	 * @throws IOException
	 */
	private String readMessagesFromServer(String message) throws IOException {
		try {
			message = (String) input.readObject();
		} catch (ClassNotFoundException classNotFoundException) {
			System.out.println(classNotFoundException.getMessage());
		}
		return message;
	}
	
	/**
	 * Sends messages to the server
	 * 
	 * @param message	a String, the message sent to/from server
	 */
	private void sendData(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ioException) {
			System.out.println(ioException.getMessage());
		}
	}
	
	public static void main(String args[]) {
		new Client();
	}
}