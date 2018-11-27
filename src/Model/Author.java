package Model;

import java.sql.SQLException;
import java.time.LocalDate;

import Database.BookTableGateway;
import javafx.beans.property.SimpleStringProperty;

public class Author 
{

	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private LocalDate dateOfBirth;
	
	private String gender;
	
	private String webSite;
	
	public Author()
	{
		id = 0;
		firstName = null;
		lastName = null;
		dateOfBirth = null;
		gender = null;
		webSite = null;
	}
	
	public void addAuthor(BookTableGateway gateway) throws SQLException
	{
		gateway.addAuthorToDB(this);
	}
	
	public void deleteAuthor(BookTableGateway gateway) throws SQLException
	{
		gateway.deleteAuthorFromDB(this);
	}
	
	public void updateAuthor(BookTableGateway gateway) throws SQLException
	{
		gateway.updateAuthorInDB(this);
	}
	
	public int getId() 
	{
		return id;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public String getFirstName() 
	{
		return firstName;
	}
	
	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}
	
	public String getLastName() 
	{
		return lastName;
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	
	public LocalDate getDateOfBirth() 
	{
		return dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) 
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getGender() 
	{
		return gender;
	}
	
	public void setGender(String gender) 
	{
		this.gender = gender;
	}
	
	public String getWebSite() 
	{
		return webSite;
	}
	
	public void setWebSite(String webSite) 
	{
		this.webSite = webSite;
	}

	@Override
	public String toString() 
	{
		return String.format("%s %s", firstName, lastName);
	}
	
}
