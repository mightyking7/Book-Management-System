package Model;

/**
 * Publisher object which is used with the BookDetailController ComboBox
 * @author HercHja
 *
 */


public class Publisher {

	/**
	 * Stores the publisher name and id from the Publisher DB table
	 * Getters and setters are public
	 */
	
	private String name;
	private int id;
	
	public Publisher(String name)
	{
		this.name = name;
	}
	
	public Publisher()
	{
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String toString()
	{
		return String.format("%s", this.getName());
	}
	
}
