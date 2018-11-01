package Controller;

/**
 * Defines the behavior of editable dialog.
 * EditableView's must enforce Pessimistic locking so that one client
 * can edit a selected record.
 * @author isaacbuitrago
 */
public interface EditableView 
{
	
	/**
	 * Determines if the fields of an editable Dialog have been changed
	 * @return
	 */
	public boolean hasChanged();
	
	/**
	 * Locks a book for pessimistic locking.
	 */
	public void lockRecord();
	
	/**
	 * Unlocks book so other waiting client's can edit the book.
	 */
	public void unlockRecord();
}
