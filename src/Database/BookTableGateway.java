package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.BookModel;

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
	
	public BookTableGateway(Connection conn)
	{
		this.conn = conn;
	}
	
	public List<BookModel> getBooks() throws SQLException
	{
		String sql = "select * from Books";
		
		ResultSet keys;
		
		List<BookModel> books = new ArrayList<BookModel>();
		
		stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
		
		rs = stmt.executeQuery();
		
		keys = stmt.getGeneratedKeys();
		
		keys.next();
		
		while(rs.next())
		{
			BookModel book = new BookModel();
			
			book.setId(keys.getRow());
			
			book.setTitle(rs.getString("title"));
			
			book.setSummary(rs.getString("summary"));
			
			book.setYearPublished(rs.getInt("year_published"));
			
			book.setIsbn(rs.getString("isbn"));
			
			Date date = rs.getDate("date_added");
			
			LocalDateTime dateAdded = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
			
			book.setDateAdded(dateAdded);
			
			books.add(book);	
		}
		
		return (books);
	}
	
	
	
}
