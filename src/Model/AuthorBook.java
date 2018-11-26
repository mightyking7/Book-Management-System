package Model;

import java.math.BigDecimal;
import javafx.beans.property.SimpleIntegerProperty;

public class AuthorBook {

	private Author author;
	private Book book;
	private SimpleIntegerProperty royalty;
	private boolean newRecord = true;
	
	public AuthorBook()
	{
		author = null;
		book = null;
		royalty = new SimpleIntegerProperty(0);
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public int getRoyalty() {
		
		return royalty.get();
	}

	public void setRoyalty(int royalty) {
		
		this.royalty.set(royalty);
	}

	public boolean isNewRecord() {
		return newRecord;
	}

	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	}
	
	/**
	 * Used to return the string representation of a Book
	 */
	@Override
	public String toString()
	{
		return String.format("%s %s", this.getAuthor().getFirstName(), this.getAuthor().getLastName());
	}
}
