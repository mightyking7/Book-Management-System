package Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;


public class Book 
{	
	private int id;
	
	private String title;
	
	private String summary;
	
	private int yearPublished;
	
	private String isbn;
	
	private LocalDateTime dateAdded;
	
	private boolean dateWasSet;
	
	public Book()
	{
		id = 0;
		title = null;
		summary = null;
		yearPublished = 0;
		isbn = null;
		dateAdded = null;
		dateWasSet = false;
	}
	
	/**
	 * Save function which uses validation methods to validate book requirements in the current book object and if error found throws exception
	 * @throws Exception
	 * @throws SQLException
	 */
	
	public void save(boolean updateDatabase) throws SQLException,Exception
	{
		
		if(!titleIsValid())
		{
			throw new Exception("Title length error");
		}
		
		if(!summaryIsValid())
		{
			throw new Exception("Summary length error");
		}
		
		if(!yearPublishedIsValid())
		{
			throw new Exception("Year Published past current year error");
		}
		
		if(!isbnIsValid())
		{
			throw new Exception("ISBN length error");
		}
		
		if(!dateAddedIsValid())
		{
			throw new Exception("Date added was not set error");
		}
		
		if(!updateDatabase)
		{
			throw new SQLException("Error saving book to database");
		}
	
	}
	
	public boolean titleIsValid()
	{
		if(getTitle().length() < 1 || getTitle().length() > 255)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean summaryIsValid()
	{
		if(getSummary().length() > 65536)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean yearPublishedIsValid()
	{
		if(getYearPublished() > (int)Calendar.getInstance().get(Calendar.YEAR))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean isbnIsValid()
	{
		if(getIsbn().length() > 13)
		{
			return false;
		}
		
		return true;
	}
	
	public boolean dateAddedIsValid()
	{
		if(!dateWasSet)
		{
			return false;
		}
		
		return true;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public int getYearPublished() {
		return yearPublished;
	}
	public void setYearPublished(int yearPublished) {
		this.yearPublished = yearPublished;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public LocalDateTime getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDateTime dateAdded) {
		
		if(this.dateAdded == null && !dateWasSet)
		{
			this.dateWasSet = true;
			this.dateAdded = dateAdded;
		}
	}
	
	/**
	 * Used to return the string representation of a Book
	 */
	@Override
	public String toString()
	{
		return String.format("Book Title: %s, Year Published: %d", this.getTitle(), this.getYearPublished());
	}

}
