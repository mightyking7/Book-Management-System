package Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import Database.BookTableGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


/**
 * Book has an id, title, summary, year of publication,
 * isbn, and a date that is was entered in the system.
 *
 *
 */
public class Book 
{	
	private int id;
	
	private String title;
	
	private String summary;
	
	private int yearPublished;
	
	private String isbn;
	
	private LocalDateTime dateAdded;
	
	private LocalDateTime lastModified;
	
	private boolean dateWasSet;
	
	private BookTableGateway gateway;
	
	/**
	 * Book NO-ARG constructor
	 */
	public Book()
	{
		this.id = 0;
		
		this.title = null;
		
		this.summary = null;
		
		this.yearPublished = 0;
		
		this.isbn = null;
		
		this.dateAdded = null;
		
		this.dateWasSet = false;
		
		this.lastModified = null;
	}
	
	/**
	 * Gets the DB gateway
	 * @param gateway
	 */
	public Book(BookTableGateway gateway)
	{
		this.gateway = gateway;
	}
	
	/**
	 * Save function which uses validation methods to validate book requirements in the current book object and if error found throws exception
	 * @throws Exception if book contains invalid data
	 * @throws SQLException if error occurred with database
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
		
		// insert a new book, update current ones
		if(id == 0)
		{
			id = gateway.insertBook(this);
		}
		else
		{
			if(!dateAddedIsValid())
			{
				throw new Exception("Date added was not set error");
			}
			
			gateway.updateBook(this);
		}
	
	}
	
	/**
	 * title validation method
	 * checks if title length >= 1 or <= 255
	 * @return
	 */
	public boolean titleIsValid()
	{
		if(!(title.length() >= 1 && title.length() <= 255))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Summary validation method
	 * checks if the summary length is > 65536 
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
	 * Checks the year published off the boo
	 * Makes sure no books are published with a post-current date
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
	 * Checks if the ISBN is valid on the book
	 * Makes sure no ISB length is > 13
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
	 * Gets the date Added and makes sure it is set
	 * if date is set twice then original date will always be preserved
	 * @return
	 */
	public boolean dateAddedIsValid()
	{
		if(!dateWasSet)
		{
			return false;
		}
		
		return true;
	}
	
	public ObservableList<AuditTrailEntry> getAuditTrail(BookTableGateway gateway) throws SQLException
	{
		return FXCollections.observableList(gateway.fetchAuditTrail(this));
	}
	
	public void updateAuditTrailEntry(String msg) throws SQLException
	{
		gateway.createNewAuditTrailEntry(this,msg);
	}
	
	public void updateBookModel(String bookTitle, String summary, int yearPublished, String isbn) throws SQLException
	{
			String compare1 = this.getTitle();
			String compare2 = bookTitle;
			compare1.replaceAll("\\s+","");
			compare2.replaceAll("\\s+","");
			
			if(compare1.compareTo(compare2) == 1)
			{
				this.updateAuditTrailEntry("Book Title changed from " + this.getTitle() + " to " + bookTitle);
				this.setTitle(bookTitle);
			}
			else
			{
				this.setTitle(bookTitle);
			}
			
			compare1 = this.getSummary();
			compare2 = summary;
			compare1.replaceAll("\\s+","");
			compare2.replaceAll("\\s+","");
			
			if(compare1.compareTo(compare2) == 1)
			{
				this.updateAuditTrailEntry("Summary changed from " + this.getSummary() + " to " + summary);
				this.setSummary(summary);
			}
			else
			{
				this.setSummary(summary);
			}
		  
			if(this.getYearPublished() != yearPublished)
			{
				this.updateAuditTrailEntry("Year Published changed from " + this.getYearPublished() + " to " + yearPublished);
				this.setYearPublished(yearPublished);
			}	
			else
			{
				this.setYearPublished(yearPublished);
			}
			
			if(this.getIsbn().compareTo(isbn) == 1)
			{
				this.updateAuditTrailEntry("isbn changed from " + this.getIsbn() + " to " + isbn);
				this.setIsbn(isbn);
			}	
			else
			{
				this.setIsbn(isbn);
			}
		 
	}
	
	/**
	 * Getters and setters below
	 */
	
	public int getId() 
	{
		return id;
	}
	
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	
	public String getTitle() 
	{
		return title;
	}
	
	
	public void setTitle(String title) 
	{
		this.title = title;
	}
	
	public String getSummary() 
	{
		return summary;
	}
	
	public void setSummary(String summary) 
	{
		this.summary = summary;
	}
	
	public int getYearPublished() 
	{
		return yearPublished;
	}
	
	public void setYearPublished(int yearPublished) 
	{
		this.yearPublished = yearPublished;
	}
	
	public String getIsbn()
	{
		return isbn;
	}
	
	public void setIsbn(String isbn)
	{
		this.isbn = isbn;
	}
	
	public LocalDateTime getDateAdded() 
	{
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
	
	public LocalDateTime getLastModified() 
	{
		return lastModified;
	}

	public void setLastModified(LocalDateTime lastModified) 
	{
		this.lastModified = lastModified;
	}

	public BookTableGateway getGateway() 
	{
		return gateway;
	}

	public void setGateway(BookTableGateway gateway) 
	{
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
