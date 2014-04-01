package server;

import java.io.BufferedWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import util.Book;

/**
 * 
 * @author Daniel Rustad Johansen
 * 
 */
public class Question {
	private ArrayList<Book> books;
	private final static String QAUTHOR = "Hvem har skrevet ";
	private Book book;
	private Random rand;

	public Question(ArrayList<Book> books) {
		this.books = books;
		rand = new Random();
	}
	
	/**
	 * Picks a random book from a list of books
	 */
	public void findBook() {
		if (books.size() > 0) {
			int randInt = rand.nextInt(books.size());
			book = books.get(randInt);
		}
	}

	/**
	 * Creates and returns a question
	 * @return		a String, the question asked
	 */
	public String getQuestion() {
		return QAUTHOR + book.getTitle() + "?";
	}

	/**
	 * Returns the answer
	 * @return		a String, name of author
	 */
	public String getAnswer() {
		return book.getAuthor();
	}

}
