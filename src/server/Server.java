package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

import util.Book;
import util.BookHandler;
import util.ConnectToDb;

/**
 * Server
 * 
 * @author Daniel Rustad Johansen
 *
 */
public class Server {
	private ArrayList<String> valg;
	private ArrayList<ClientConnection> klienter;
	private String[] login = { "root", "password" };
	private ArrayList<Book> books;
	private int id = 1000;
	private BookHandler bookHandler;
	
	public Server() {
		klienter = new ArrayList<ClientConnection>(0);
		valg = new ArrayList<String>();
		books = new ArrayList<Book>();
		
		try (ServerSocket server = new ServerSocket(8000);
				ConnectToDb db = new ConnectToDb(login[0], login[1]);
				Connection connection = db.getConnection()) {
			
			bookHandler = new BookHandler(connection);
			books = bookHandler.getAllBooks();
			
			System.out.println(("Venter på klientkontakt..."));
			while (true) {
				try  {
					Socket klientkontakt = server.accept();
					System.out.println("Kontakt opprettet...");
					System.out.println("Klientkontaktadresse: "
							+ klientkontakt.getInetAddress());
					
					ClientConnection kForbindelse = new ClientConnection(klientkontakt, ++id, books);
					/* 
					 * Adds the client to a list,
					 * possible to expand the program
					 */
					klienter.add(kForbindelse);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (SQLException e) {
			System.out.println("Connection to DB failed!");
			e.printStackTrace();
		}
	}
		public static void main(String args[]) {
			new Server();
	}
}
