package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Responsible for entering a table and fetch
 * all the data related to the specific table
 * 
 * @author Daniel Rustad Johansen
 *
 */
public class BookHandler {
	private Connection connection;
	private static final String query = "SELECT author, title, isbn, pages, published FROM booklist";
	
	public BookHandler(Connection connection) {
		this.connection = connection;
		
	}
	
	/**
	 * Executes a sql query and fetches all data in a
	 * table, which it then uses to create Book-objects
	 * which is stored and returned in an ArrayList
	 * 
	 * @return		an ArrayList<Book>, a list containing Book-objects
	 */
	public ArrayList<Book> getAllBooks() {
		ArrayList<Book> listOfBooks = new ArrayList<Book>();
		
		try (PreparedStatement pStmt = connection.prepareStatement(query);
				ResultSet rs = pStmt.executeQuery()) {
			while (rs.next()) {
				listOfBooks.add(new Book(
							rs.getString(1), 
							rs.getString(2),
							rs.getString(3), 
							rs.getInt(4), 
							rs.getInt(5)));
			}
		} catch (SQLException e) {
			System.out.println("Failed to send query");
			e.printStackTrace();
		}
		
		return listOfBooks;
	}
	
}
