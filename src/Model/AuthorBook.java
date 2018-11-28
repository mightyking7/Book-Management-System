package Model;

import java.math.BigDecimal;
import javafx.beans.property.SimpleIntegerProperty;

public class AuthorBook {

	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord = true;
	
	public final static int ROYALTY_PRECISION = 100000;  // precision for royalty
	
	public AuthorBook()
	{
		author = null;
		book = null;
		royalty = 0;
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
	
	public BigDecimal getRoyalty() {
		
		return BigDecimal.valueOf(royalty).movePointLeft(3);
	}

	public void setRoyalty(int royalty) {
		
		this.royalty = royalty;
	}
	
	public void setRoyalty(BigDecimal newValue) 
	{
		this.royalty = (int) (newValue.doubleValue() * ROYALTY_PRECISION);
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
