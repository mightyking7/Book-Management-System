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
	
	public ObservableList<Publisher> fetchPublishers() throws SQLException
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
	
		return FXCollections.observableList(PublisherEntries);
	}
	
	public int getBookAndPublisherConnection(Book book) throws SQLException
	{
		sql = "SELECT * FROM Books WHERE id=" + book.getId() + " AND publisher_id IN (SELECT id FROM Publishers GROUP BY id HAVING COUNT(*) > 1)";
		
		stmt = conn.prepareStatement(sql);
		
		result = stmt.executeQuery();
		
		Publisher publisher = new Publisher();
		
		while(result.next())
		{
	
			publisher.setId(result.getInt("id"));
			
			publisher.setName(result.getString("publisher"));
	
		}
		
		return publisher.getId();
		
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
