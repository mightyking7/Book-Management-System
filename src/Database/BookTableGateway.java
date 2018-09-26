package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Model.Book;

/**
 * Sep 17, 2018
 * isaacbuitrago
 * 
 * Used as an abstraction between the Book model and the database
 */
public class BookTableGateway 
{
	private Connection conn;		// connection to data store
	
	private PreparedStatement stmt;	// statement to execute

	private ResultSet rs;			// result returned from MySQL
	
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
	public ArrayList<Book> getBooks() throws SQLException
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
			
			System.out.println(book);
		}
		
		return (books);
	}
	
	/**
	 * Used to update a book in the database through the model save function
	 * @return
	 * @throws SQLException
	 */
	
	public boolean UpdateBook(Book book) throws SQLException
	{
		try{
			//String query = "UPDATE books SET title = ? , " + "comment = ? " + "WHERE id = " + book.getID();
			//PreparedStatement preparedStmt = conn.prepareStatement(query);
			//preparedStmt.setString(1, book.getTitle());
			//preparedStmt.setString(2, book.getComment());
			//...
			//preparedStmt.executeUpdate();
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	/**
	 * Used to remove a book in the database
	 * @return
	 * @throws SQLException
	 */
	
	public void deleteMethod()
	{
		
	}
	
	
}
