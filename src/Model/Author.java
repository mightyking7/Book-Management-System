package Model;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;

public class Author 
{

	private int id;
	
	private SimpleStringProperty firstName;
	
	private SimpleStringProperty lastName;
	
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
		return firstName.get();
	}
	
	public void setFirstName(String firstName) 
	{
		this.firstName.set(firstName);
	}
	
	public String getLastName() 
	{
		return lastName.get();
	}
	
	public void setLastName(String lastName) 
	{
		this.lastName.set(lastName);
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
