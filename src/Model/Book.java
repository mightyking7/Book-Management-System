package Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Calendar;

import Database.BookTableGateway;


public class Book 
{	
	private int id;
	
	private String title;
	
	private String summary;
	
	private int yearPublished;
	
	private String isbn;
	
	private LocalDateTime dateAdded;
	
	private boolean dateWasSet;
	
	private BookTableGateway gateway;
	
	/**
	 * 
	 */
	public Book()
	{
		
	}
	
	/**
	 * 
	 * @param gateway
	 */
	public Book(BookTableGateway gateway)
	{
		this.gateway = gateway;
	}
	
	/**
	 * Save function which uses validation methods to validate book requirements in the current book object and if error found throws exception
	 * @throws Exception
	 * @throws SQLException
	 */
	
	public void save() throws SQLException,Exception
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
		
//		if(!dateAddedIsValid())
//		{
//			throw new Exception("Date added was not set error");
//		}
//		
		// insert the book if the id is 0
		if(id == 0)
		{
			id = gateway.insertBook(this);
		}
		else
		{
			gateway.updateBook(this);
		}
	
	}
	
	/**
	 * validates the title
	 * @return
	 */
	public boolean titleIsValid()
	{
		// white list valid data, don't check for the entire set of invalid data
		if(!(title.length() >= 1 && title.length() <= 255))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean summaryIsValid()
	{
		if(getSummary().length() > 65536)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Used to validate the year
	 * @return
	 */
	public boolean yearPublishedIsValid()
	{
		if(yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR) || yearPublished < 0)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isbnIsValid()
	{
		if(getIsbn().length() > 13)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 */
	public boolean dateAddedIsValid()
	{
		if(!dateWasSet)
		{
			return false;
		}
		
		return true;
	}
	
	public int getId() 
	{
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) 
	{
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTitle() 
	{
		return title;
	}
	
	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) 
	{
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
	
	public void setDateAdded(LocalDateTime dateAdded) 
	{
		
		if(this.dateAdded == null && !dateWasSet)
		{
			this.dateWasSet = true;
			this.dateAdded = dateAdded;
		}
	}
	
	
	public BookTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(BookTableGateway gateway) {
		this.gateway = gateway;
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
