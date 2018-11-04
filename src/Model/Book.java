package Model;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
	
	private Publisher publisher;
	
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
		
		this.publisher = null;
		
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
	 * Save function which uses validation methods to validate book requirements 
	 * in the current book object, if an error is found throws exception.
	 * 
	 * Precondition: The updateBookModel method has been used to set Book attributes or
	 * all Book attributes have been set separately 
	 * 
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
			this.updateAuditTrailEntry("Book Added");
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
	
	/**
	 * Method which fetches the Audit Trail for the given book and passes it to the Audit Trail View's ObservableList
	 * @param gateway
	 * @return
	 * @throws SQLException
	 */
	
	public ObservableList<AuditTrailEntry> getAuditTrail(BookTableGateway gateway) throws SQLException
	{
		return FXCollections.observableList(gateway.fetchAuditTrail(this));
	}
	
	/**
	 * Method which calls the create new Audit Trail method through the Book Table Gateway if book object has been updated by the user
	 * @param msg
	 * @throws SQLException
	 */
	
	public void updateAuditTrailEntry(String msg) throws SQLException
	{
		gateway.createNewAuditTrailEntry(this,msg);
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
	
	public Publisher getPublisher() 
	{
		return publisher;
	}

	public void setPublisher(Publisher publisher) 
	{
		this.publisher = publisher;
	}

	/**
	 * Checks and sets any value from the Book Detail Controller save method to the book model
	 * Checks the values for inconsistencies and if so updates the value and calls the updateAuditTrailEntry method
	 * Parses each string to remove empty space which can cause false update positives when passed by the DB
	 * @param bookTitle
	 * @param summary
	 * @param yearPublished
	 * @param isbn
	 * @throws SQLException
	 */
	public void updateBookModel(String bookTitle, String summary, int yearPublished, String isbn, Publisher publisher) throws SQLException
	{
		
		/*
		 * Change and audit Book attributes only when the attribute
		 * has been added to the Book model or has changed.
		 */
			
			if(this.getId() != 0)
			{
				if(this.getTitle().compareToIgnoreCase(bookTitle) != 0)
				{
					this.updateAuditTrailEntry("Book Title changed from " + this.getTitle() + " to " + bookTitle);
				}
			
				if(this.getSummary().compareToIgnoreCase(summary) != 0)
				{
					this.updateAuditTrailEntry("Summary changed from " + this.getSummary() + " to " + summary);
				}
			  
				if( this.getYearPublished() != yearPublished)
				{
					this.updateAuditTrailEntry("Year Published changed from " + this.getYearPublished() + " to " + yearPublished);
				}	
				
				if(this.getIsbn().compareToIgnoreCase(isbn) != 0)
				{
					this.updateAuditTrailEntry("isbn changed from " + this.getIsbn() + " to " + isbn);
				}
				
				Publisher p1 = this.getPublisher();
				Publisher p2 = publisher;
				
				if( p1.getId() != p2.getId())
				{
					this.updateAuditTrailEntry("Publisher changed from " + p1.getName() + " to " + publisher.getName());
				}
			}
		
			this.setTitle(bookTitle);
			this.setSummary(summary);
			this.setYearPublished(yearPublished);
			this.setIsbn(isbn);
			this.setPublisher(publisher);
	}

	/**
	 * Used to return the string representation of a Book
	 */
	@Override
	public String toString()
	{
		return String.format("%s", this.getTitle());
	}

}
