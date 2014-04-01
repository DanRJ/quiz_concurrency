package util;

/**
 * Book class
 * 
 * @author Daniel Rustad Johansen
 *
 */
public class Book {
	private String author;
	private String title;
	private String isbn;
	private int pages;
	private int published;
	
	public Book() {
		this("","","",0,0);
	}
	
	public Book(String author, String title, String isbn, int pages, int published) {
		setAuthor(author);
		setTitle(title);
		setIsbn(isbn);
		setPages(pages);
		setPublished(published);
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public int getPublished() {
		return published;
	}
	public void setPublished(int published) {
		this.published = published;
	}
	
}
