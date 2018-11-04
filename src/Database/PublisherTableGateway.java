package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Model.Book;
import Model.Publisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Publisher Table Gateway which handles any connection to the DB between the Book Detail Controller and the Publisher DB
 * @author HercHja
 *
 */

public class PublisherTableGateway {

	private Connection conn;		// connection to data store
	
	private PreparedStatement stmt;	// statement to execute

	private ResultSet result;		// result returned from MySQL
	
	private String sql;				// SQL statement to execute against the database
	
	//Manual DB connection Constructor
	
	public PublisherTableGateway(Connection conn)
	{
		this.conn = conn;
	}
	
	//Automatic DB connection Constructor
	
	public PublisherTableGateway() throws SQLException
	{
		this.conn = DBConnection.getInstance().getConnection();
	}
	
	/**
	 * Fetch publishers method which gets all publishers and displays them in the Book Detail Controller ComboBox
	 * Uses a SQL statement and the Publisher Model to store and pass an ObservableList of Publishers
	 * @return
	 * @throws SQLException
	 */
	
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
	
	/**
	 * Method which joins the Book DB Table and the Publisher DB Table to populate the default ComboBox selection in the Book Detail Controller when opening the Book Detail View
	 * @param book
	 * @return
	 * @throws SQLException
	 */
	
	public int getBookAndPublisherConnection(Book book) throws SQLException
	{
		sql = "SELECT Books.publisher_id"
			   + " FROM Books"
			   + " INNER JOIN Publishers"
			   + " ON Book.publisher_id = Publishers.id"
			   + " AND Book.id = " + book.getId();
				     
		stmt = conn.prepareStatement(sql);
		
		result = stmt.executeQuery();
		/*
		Publisher publisher = new Publisher();
		
		while(result.next())
		{
	
			publisher.setId(result.getInt("id"));
			
			publisher.setName(result.getString("publisher"));
	
		}
		*/
		result.next();
		return result.getInt("publisher_id");
		
	}
	
	/**
	 * Updates the Book DB Table with the new Publisher listed in the ComboBox selection in the Book Detail Controller
	 * Gets the book ID and the ComboBox index/publisher_id through parameters passed
	 * @param book
	 * @param index
	 * @throws SQLException
	 */
	
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
