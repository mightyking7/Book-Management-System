package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * AuditTrailEntry model which holds the DB object ID, date when it was edited and also the message of the edit
 * @author HercHja
 *
 */

public class AuditTrailEntry {

	private int ID;
	private LocalDateTime dateAdded;
	private String message;
	
	public AuditTrailEntry()
	{
		ID = 0;
		dateAdded = null;
		message = null;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString()
	{
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
		return String.format("Timestamp: %s, Message: %s", this.getDateAdded().format(formater), this.getMessage());
	}
	
}