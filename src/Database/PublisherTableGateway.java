package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import Model.AuditTrailEntry;
import Model.Book;
import Model.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PublisherTableGateway {

	private Connection conn;		// connection to data store
	
	private PreparedStatement stmt;	// statement to execute

	private ResultSet result;		// result returned from MySQL
	
	private String sql;				// SQL statement to execute against the database
	
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
	 * Retrieves the Publisher id for a given book
	 * @param bookId database id of the Book to retrieve the Publisher for
	 * @return Id of the publisher
	 * @throws SQLException
	 */
	public int getBooksPublisher(int bookId) throws SQLException
	{
		
		int publisherId = 0;
		
		sql = "select p.id, p.publisher from `Publishers` as p inner join Books as b on b.publisher_id = p.id WHERE b.id = ?";
		
		stmt = conn.prepareStatement(sql);
		
		stmt.setInt(1, bookId);
		
		result = stmt.executeQuery();
		
		Publisher publisher = new Publisher();
		
		if(result.next())
		{
			publisherId = result.getInt("id");
		}
		
		return publisherId;
	}
	
	public void updatePublisherIDInBooksTable(Book book, int index) throws SQLException
	{
			sql = "UPDATE Books "
	               + "SET publisher_id = ? "
	               + "WHERE id =" + book.getId();
			
			PreparedStatement preparedStmt = conn.prepareStatement(sql);
			preparedStmt.setInt(1, index);
			
			try {
				
				preparedStmt.executeUpdate();
			
			} catch(SQLException e)
			{
				conn.rollback();
				
				throw e;
			} finally {
		
				conn.setAutoCommit(true);
			}
	}
	
}
