package Test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import Database.BookTableGateway;
import Database.DBConnection;
import Model.Book;

/**
 * Sep 22, 2018
 * 
 * Test BookTableGateway
 * isaacbuitrago
 */

public class BookTableGatewayTest 
{
	DBConnection conn;
	
	BookTableGateway gateway;
	
	public BookTableGatewayTest() throws SQLException
	{
		conn = DBConnection.getInstance();
		
		gateway = new BookTableGateway(conn.getConnection());	
	}
	
	@Test
	public void testGetBooks()
	{
		try {
			
		ArrayList<Book> books = (ArrayList<Book>) gateway.getBooks();
		
		assert(books != null);
		
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}

}
