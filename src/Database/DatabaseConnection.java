package Database;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/*
 * Sep 18, 2018
 * 
 * Maintains single instance to the MySql connection
 * 
 * @author isaacbuitrago
 */

public class DatabaseConnection 
{
	private MysqlDataSource mysql;	// mysql instance
	
	private Connection conn;		// connection to the database
	
	private static DatabaseConnection instance;
	
	private DatabaseConnection() throws SQLException
	{
		mysql = new MysqlDataSource();
		
		mysql.setUrl("dbc:mysql://easel2.fulgentcorp.com:3306/hwt460");
		
		mysql.setUser("hwt460");
		
		mysql.setPassword("SBRA6V2gGPqrnuqlgZzU");
		
		conn = mysql.getConnection();
	}
	
	public static DatabaseConnection getInstance() throws SQLException
	{
		if(instance == null)
		{
			instance = new DatabaseConnection();
		}
		
		return(instance);
	}
	
	private Connection getConnection()
	{
		return (conn);
	}

}
