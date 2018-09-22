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

public class DBConnection 
{
	private MysqlDataSource mysql;	// mysql instance
	
	private Connection conn;		// connection to the database
	
	private static DBConnection instance;
	
	/**
	 * Constructor establishes the database connection
	 * @throws SQLException if the connection could not be established
	 */
	private DBConnection() throws SQLException
	{
		mysql = new MysqlDataSource();
		
		mysql.setUrl("dbc:mysql://easel2.fulgentcorp.com:3306/hwt460");
		
		mysql.setUser("hwt460");
		
		mysql.setPassword("SBRA6V2gGPqrnuqlgZzU");
		
		conn = mysql.getConnection();
	}
	
	/**
	 * Used to get the single connection to the database
	 * @return
	 * @throws SQLException if the connection could not be established
	 */
	public static DBConnection getInstance() throws SQLException
	{
		if(instance == null)
		{
			instance = new DBConnection();
		}
		
		return(instance);
	}
	
	/**
	 * 
	 * @return connection to the database
	 */
	// close the connection as soon as possible
	public Connection getConnection()
	{
		return (conn);
	}

}
