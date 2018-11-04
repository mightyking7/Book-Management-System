package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Model.Publisher;

public class PublisherTableGateway {

	private Connection conn;		// connection to data store
	
	private PreparedStatement stmt;	// statement to execute

	private ResultSet result;		// result returned from MySQL
	
	private String sql;				// SQL statement to execute against the database
	
	public static final int DEFAULT_PUBLISHER_ID = 1;	// Database id of the Unknown publisher 
	
	public PublisherTableGateway(Connection conn)
	{
		this.conn = conn;
	}
	
	public PublisherTableGateway() throws SQLException
	{
		this.conn = DBConnection.getInstance().getConnection();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Publisher> fetchPublishers() throws SQLException
	{
		sql = "SELECT * FROM Publishers"; 
		
		ArrayList<Publisher> PublisherEntries = new ArrayList<Publisher>();
		
		stmt = conn.prepareStatement(sql);
		
		result = stmt.executeQuery();
		
		while(result.next())
		{

			Publisher publisher = new Publisher();
			
			publisher.setId(result.getInt("id"));
			
			publisher.setName(result.getString("publisher"));
			
			PublisherEntries.add(publisher);	
		}
	
		return PublisherEntries;
	}
	
	/**
	 * Used to retrieve the Publisher of a Book
	 * 
	 * @param bookId to use for an inner join on the Publisher table
	 * @return Publisher for the Book
	 * @throws SQLException 
	 */
	public Publisher getBookPublisher(int bookId) throws SQLException
	{
		
		Publisher publisher = new Publisher();
		
		String sql = "select p.id, p.publisher from Publishers as p "
					+ "inner join Books as b on b.publisher_id = p.id where b.id = ?";
		
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, bookId);
		
		result = stmt.executeQuery();
		
		if(result.next())
		{
			publisher.setId(result.getInt("id"));
			
			publisher.setName(result.getString("publisher"));
		}
		
		return publisher;		
	}
	
}
