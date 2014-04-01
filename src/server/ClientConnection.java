package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import util.Book;

/**
 * Responsible for handling ClientConnections
 * 
 * @author Daniel Rustad Johansen
 * 
 */
public class ClientConnection implements Runnable {
	private Socket clientContact;
	private int id;
	private ArrayList<Book> books;
	private Question question;
	private int counter;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	public ClientConnection(Socket clientContact, int id, ArrayList<Book> books) {
		this.clientContact = clientContact;
		this.id = id;
		this.books = books;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			output = new ObjectOutputStream(clientContact.getOutputStream());
			output.flush();
			input = new ObjectInputStream(clientContact.getInputStream());

			Object message = "Vil du delta i Quiz? (ja/nei)!";
			output.writeObject(message);
			question = new Question(books);
			
			do {
				message = input.readObject();
				askQuestions(message);
				output.flush();
			} while (!(("slutt").equals(message)));

			System.out.println("Server - Stenger forbindelsen til "
					+ clientContact.getInetAddress());
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Class not found!");
			e.printStackTrace();
		} finally {
			exit();
		}
	}

	/**
	 * Disconnects the ClientConnection from the server by closing
	 * Sockets and Input/Output streams
	 */
	private void exit() {
		try {
			clientContact.close();
			output.close();
			input.close();
		} catch (IOException ioException) {
			System.out.println("Det skjedde en feil " +
								"under lukking av output/input/server!");
		}
	}
	
	/**
	 * Creates a farewell message with points
	 * achieved by the user.
	 * 
	 * @return	a String, a message that thanks the user for participating.
	 */
	private String goodbyeMessage() {
		return "Takk for at du deltok!\nDu fikk " + counter + " riktige!";
	}
	
	/**
	 * This method is responsible for asking questions
	 * and handling basic user input
	 * 
	 * @param message		an Object, users input, treated as a String
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void askQuestions(Object message) throws IOException,
			ClassNotFoundException {
		switch (inputCheck(message)) {
		case "ja":
			question.findBook();

			message = question.getQuestion();
			output.writeObject(message);

			message = input.readObject();
			message = checkAnswer(message);

			message += "\nVil du fortsette? (ja/nei)";
			output.writeObject(message);
			output.flush();

			message = input.readObject();
			
			// Rekursjon ftw!
			askQuestions(message);

			break;
		case "nei":
			output.writeObject(goodbyeMessage() + " slutt");
			break;
		case "":
			output.writeObject(wrongInput());
			break;
		}
	}
	
	/**
	 * If user input is wrong, it sends back a message.
	 * 
	 * @return	a String, a message to the user the correct input to write.
	 */
	private Object wrongInput() {
		return "Du må skrive ja eller nei!";
	}
	
	/**
	 * Checks the user input
	 * 
	 * @param melding	an Object treated as a String, message from user
	 * @return			a String, user input
	 */
	private String inputCheck(Object melding) {
		if (melding.equals("ja")) {
			return "ja";
		} else if (melding.equals("nei")) {
			return "nei";
		}
		return "";
	}
	
	/**
	 * Checks users answers to questions
	 * 
	 * @param input		an Object, users answer
	 * @return			a String, correct or wrong.
	 */
	private String checkAnswer(Object input) {
		String inputAnswer = input.toString();
		
		if (inputAnswer.toString().equals("ja")) {
			return "Really?";
		} else if (inputAnswer.toString().equals("nei")) {
			return "No, u play!";
		}
		
		if (isCorrectAnswer(inputAnswer)) {
			counter++;
			return "Riktig!";
		}
		
		return "Feil - det er " + question.getAnswer();
	}

	/**
	 * Checks the users answer against the answer from the db
	 * 
	 * @param userInput		a String, users answer
	 * @return				a boolean, either true or false
	 */
	private boolean isCorrectAnswer(String userInput) {
		String correctAnswer = question.getAnswer();
		List<String> correctAnswersWords = new ArrayList<>(
				Arrays.asList(correctAnswer.split("[, .-]+")));
		
		if (correctAnswersWords.size() < 1) {
			throw new IllegalArgumentException(
					"Correct answer has to be atleast one word!");
		}
		
		String[] split = userInput.toUpperCase().split("[^a-zA-ZæøåÆØÅ]+");
		for (int i = 0; i < split.length; i++) {
			correctAnswersWords.remove(split[i]);
			if (i + 1 != split.length && correctAnswersWords.size() == 0) {
				return false;
			}
		}
		return correctAnswersWords.size() == 0;
	}
}