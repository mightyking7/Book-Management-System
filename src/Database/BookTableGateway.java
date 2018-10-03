package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import org.apache.logging.log4j.core.Logger;

import Model.Book;

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

	private ResultSet rs;			// result returned from MySQL
	
	private static Logger logger;
	
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
	 * @return
	 * @throws SQLException
	 */
	public List<Book> getBooks() throws SQLException
	{
		String sql = "select * from Books";
		
		ArrayList<Book> books = new ArrayList<Book>();
		
		stmt = conn.prepareStatement(sql);
		
		rs = stmt.executeQuery();
		
		while(rs.next())
		{
			Book book = new Book();
			
			book.setId(rs.getInt("id"));
			
			book.setTitle(rs.getString("title"));
			
			book.setSummary(rs.getString("summary"));
			
			book.setYearPublished(rs.getInt("year_published"));
			
			book.setIsbn(rs.getString("isbn"));
			
			LocalDateTime dateAdded = rs.getTimestamp("date_added").toLocalDateTime();
			
			book.setDateAdded(dateAdded);
			
			books.add(book);	
		}
		
		return (books);
	}
	
	/**
	 * Used to update a book in the database through the model save function
	 * @return
	 * @throws SQLException
	 */
	
	public boolean updateBook(Book book) throws SQLException
	{
		try{
			
			String query = "UPDATE Books "
	                + "SET title = ? "
	                + ",summary = ? "
	                + ",year_published = ? "
	                + ",isbn = ? "
	                + "WHERE id = " + book.getId();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, book.getTitle());
			preparedStmt.setString(2, book.getSummary());
			preparedStmt.setInt(3, book.getYearPublished());
			preparedStmt.setString(4, book.getIsbn());
			preparedStmt.executeUpdate();
			
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * Used to insert a new book into the database
	 * If the book already has an id, it is updated.
	 * Returns the unique id of the new book
	 * 
	 * @param book
	 * @throws SQLException
	 */
	public int insertBook(Book book) throws SQLException
	{
		ResultSet generatedKeys;	// id of the new book
		
		String sql = "insert into Books (title, summary, year_published, isbn) values(?, ?, ?, ?)";
		
		stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, book.getTitle());
		
		stmt.setString(2, book.getSummary());
		
		stmt.setInt(3, book.getYearPublished());
		
		stmt.setString(4, book.getIsbn());
		
		stmt.executeUpdate();
		
		generatedKeys = stmt.getGeneratedKeys();
		
		generatedKeys.next();
		
		return(generatedKeys.getInt(1));
	}
	
	/**
	 * Used to remove a book in the database
	 * @return
	 * @throws SQLException
	 */
	
	public void deleteMethod(Book book)
	{
		
		// TODO perhaps run this as a transaction
		try{
			String query = "DELETE FROM Books WHERE id = " + book.getId();
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.executeUpdate();
		}
		catch(Exception e)
		{
		
		}
	}
	
	
}
