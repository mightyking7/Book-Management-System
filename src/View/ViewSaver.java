package View;

import javafx.scene.control.Alert;

/**
 * Checks if a view with editable fields has been saved and prompts the user to
 * take action when they have unsaved changes.
 * @author isaacbuitrago
 */
public class ViewSaver 
{
	
	private boolean viewEdited;		// view was edited
	
	private boolean viewSaved;  	// save action invoked
	
	private Alert prompt;			// prompt to save, exit, or cancel
	
	/**
	 * Checks if an edited view has been saved
	 * and prompts the user to save, abort, or continue
	 * their changes while exiting an edit view.
	 */
	public void checkIfEdited() 
	{
		if(viewEdited && ! viewSaved)
		{
			//prompt = new Alert
		}
	}


	public boolean isViewEdited() 
	{
		return viewEdited;
	}


	public void setViewEdited(boolean viewEdited) 
	{
		this.viewEdited = viewEdited;
	}


	public boolean isViewSaved() 
	{
		return viewSaved;
	}


	public void setViewSaved(boolean viewSaved) 
	{
		this.viewSaved = viewSaved;
	}
	
	
}
