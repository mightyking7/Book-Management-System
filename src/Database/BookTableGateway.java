package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import Model.AuditTrailEntry;
import Model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Sep 17, 2018
 * isaacbuitrago
 * 
 * Manages interaction between the 
 * Book model and the Book table in the database.
 */
public class BookTableGateway 
{
	private Connection conn;		// connection to data store
	
	private PreparedStatement stmt;	// statement to execute

	private ResultSet result;		// result returned from MySQL
	
	private String sql;				// SQL statement to execute against the database
	
	
	/**
	 * Constructor with specified connection
	 * @param conn
	 */
	public BookTableGateway(Connection conn)
	{
		this.conn = conn;
	}
	
	/**
	 * Constructor with default connection
	 * @throws SQLException if connection could not be established 
	 */
	public BookTableGateway() throws SQLException
	{
		this.conn = DBConnection.getInstance().getConnection();
	}
	
	/**
	 * Used to retrieve all books in the database
	 * @return List of Books from the database
	 * @throws SQLException if an error occurred while interacting with the database
	 */
	public List<Book> getBooks() throws SQLException
	{
		sql = "select * from Books";
		
		ArrayList<Book> books = new ArrayList<Book>();
		
		stmt = conn.prepareStatement(sql);
		
		result = stmt.executeQuery();
		
		while(result.next())
		{
			Book book = new Book();
			
			book.setId(result.getInt("id"));
			
			book.setTitle(result.getString("title"));
			
			book.setSummary(result.getString("summary"));
			
			book.setYearPublished(result.getInt("year_published"));
			
			book.setIsbn(result.getString("isbn"));
			
			LocalDateTime dateAdded = result.getTimestamp("date_added").toLocalDateTime();
			
			LocalDateTime lastModified = result.getTimestamp("last_modified").toLocalDateTime();
			
			book.setDateAdded(dateAdded);
			
			book.setLastModified(lastModified);
			
			books.add(book);	
		}
		
		return (books);
	}
	
	/**
	 * Used to update a book in the database through the model save function.
	 * Creates a query request for the DB by getting the ID and updates the requested fields with the book object information.
	 * After the transaction is complete, autocommit is turned back on.
	 * @throws SQLException if an error occurred while interacting with the database
	 */
	public void updateBook(Book book) throws SQLException
	{
			sql = "UPDATE Books "
	               + "SET title = ? "
	               + ",summary = ? "
	               + ",year_published = ? "
	               + ",isbn = ? "
	               + "WHERE id = ?";
			
			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setString(1, book.getTitle());
			preparedStmt.setString(2, book.getSummary());
			preparedStmt.setInt(3, book.getYearPublished());
			preparedStmt.setString(4, book.getIsbn());
			preparedStmt.setInt(5, book.getId());
			
			try {
				
				preparedStmt.executeUpdate();
			
			} catch(SQLException e)
			{
				conn.rollback();
				
				throw e;
			} finally {
				
				// complete the transaction
				conn.setAutoCommit(true);
			}
	}
	
	/**
	 * Used to lock a book record in the database 
	 * so that only one user has access to it.
	 * This turns off auto commit, any users of this method
	 * should remember to turn auto commit on after a transaction.
	 * 
	 * @param book record to lock in the database
	 * @throws SQLException  if an error occurred in communicating with the database
	 */
	public void lockBook(Book book) throws SQLException
	{
		sql = "select title from Books where id = ? for update";
		
		conn.setAutoCommit(false);
		
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, book.getId());
		
		stmt.executeQuery();
	}
	
	/**
	 * Used to insert a new book into the database
	 * and set the date added timestamp on the book.
	 * If the book already has an id, it is updated.
	 * Returns the id of the new book
	 * 
	 * @param book
	 * @throws SQLException if an error occurred while interacting with the database
	 */
	public int insertBook(Book book) throws SQLException
	{
		ResultSet generatedKeys;	// id of the new book
		
		sql = "insert into Books (title, summary, year_published, isbn) values(?, ?, ?, ?)";
		
		int bookId;
		
		// if the book does not have an id, it should be updated
		if(!(book.getId() == 0))
		{
			updateBook(book);
		}
		
		stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, book.getTitle());
		
		stmt.setString(2, book.getSummary());
		
		stmt.setInt(3, book.getYearPublished());
		
		stmt.setString(4, book.getIsbn());
		
		stmt.executeUpdate();
		
		generatedKeys = stmt.getGeneratedKeys();
		
		generatedKeys.next();
		
		bookId = generatedKeys.getInt(1);
		
		ArrayList<LocalDateTime> timestamps = getBookTimeStamps(bookId);
		
		// set the date added time stamp in the book model
		book.setDateAdded(timestamps.get(0));
		
		book.setLastModified(timestamps.get(1));
		
		return(bookId);
	}
	
	
	/**
	 * Used to get the date added and last modified timestamps for a book with the given id.
	 * 
	 * @param  bookId  of the book to retrieve the timestamps for
	 * @return ArrayList<LocalDateTime> of timestamps, where the first element is the 
	 *     	   date added timestamp and the second is the last modified timestamp. 
	 * @throws SQLException if a database access error occurs or method called on a closed connection.
	 */
	public ArrayList<LocalDateTime> getBookTimeStamps(int bookId) throws SQLException
	{
		
		ArrayList<LocalDateTime> timestamps = new ArrayList<LocalDateTime>();
		
		sql = "select date_added, last_modified from Books where id = ?";
		
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, bookId);
		
		result = stmt.executeQuery();
		
		result.next();
		
		timestamps.add(result.getTimestamp("date_added").toLocalDateTime());
		
		timestamps.add(result.getTimestamp("last_modified").toLocalDateTime());
		
		return(timestamps);
	}
	
	/**
	 * Used to get the date added timestamp for a book with the given id.
	 * 
	 * @param bookId  of the book to retrieve the date added timestamp
	 * @return LocalDateTime that the book was added to the database, null if the result was empty.
	 * @throws SQLException if a database access error occurs or 
	 * 		   method called on a closed connection.
	 */
	public LocalDateTime getBookModifiedTime(int bookId) throws SQLException
	{
		sql = "select date_added from Books where id = ?";
		
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, bookId);
		
		result = stmt.executeQuery();
		
		LocalDateTime dateAdded = null;
		
		if(result.next())
		{
			dateAdded = result.getTimestamp("date_added").toLocalDateTime();
		}
		
		return(dateAdded);
	}
	
	/**
	 * Used to remove a book in the database, sets up a delete query and executes it on the requested field by getting the book object id passed
	 * @return
	 * @throws SQLException
	 */
	
	public void deleteMethod(Book book) throws SQLException
	{
			sql = "DELETE FROM Books WHERE id = " + book.getId();
			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			preparedStmt.executeUpdate();
	}

	/**
	 * Fetches the last modified timestamp of a book in the database
	 * and determines if it's locked by another transaction.
	 * 
	 * @param book to verify if locked 
	 * @return true if the book is locked by another transaction, false otherwise.
	 * @throws SQLException 
	 */
	public boolean checkBookLocked(Book book) throws SQLException 
	{	
		return ( getBookModifiedTime(book.getId()) == null);
	}
	
	public List<AuditTrailEntry> fetchAuditTrail(Book book) throws SQLException
	{
		sql = "select book_id from book_audit_trail "
				+ "WHERE id = " + book.getId();
		
		ArrayList<AuditTrailEntry> AtrailEntries = new ArrayList<AuditTrailEntry>();
		
		stmt = conn.prepareStatement(sql);
		
		result = stmt.executeQuery();
		
		while(result.next())
		{
			AuditTrailEntry ATE = new AuditTrailEntry();
			
			LocalDateTime dateAdded = result.getTimestamp("date_added").toLocalDateTime();
			
			ATE.setDateAdded(dateAdded);
			
			ATE.setMessage(result.getString("entry_msg"));
			
			AtrailEntries.add(ATE);	
		}
		   
		return AtrailEntries;
	}
	
}
