package View;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import Controller.Controller;
import Controller.EditableView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.*;

/**
 * Singleton responsible for transitioning between views
 * and handling the events associated with these transitions.
 * 
 * @author isaacbuitrago
 */
public class ViewManager 
{
	private static ViewManager instance;
	
	private BorderPane borderPane;
	
	private Controller lastController;		// last controller used 
	
	private Alert prompt;					// prompt to save, exit, or cancel
	
	
	/**
	 * Constructor
	 */
	private ViewManager()
	{
	}
	
	/**
	 * Used to access the single instance of the class and create one if needed
	 * @return
	 */
	public static ViewManager getInstance()
	{
		if(instance == null)
		{
			instance = new ViewManager();
		}
		
		return(instance);
	}
	
	/**
	 * 
	 * @return
	 */
	public BorderPane getCurrentPane() 
	{
		return borderPane;
	}

	/**
	 * Used to set the Pane that needs to manage view switching
	 * @param currentPane
	 */
	public void setCurrentPane(BorderPane currentPane) 
	{
		this.borderPane = currentPane;
	}
	
	/**
	 * Used to change the view of the current Pane 
	 * @param parent Relative URL of the view to load
	 * @param controller Controller to set for the new view
	 * @throws IOException If parent URL is not valid, null, or could not be loaded
	 * @throws NullPointerException If the managed Layout Pane is null
	 *
	 */
	public void switchView(URL parent, Controller controller) throws IOException, NullPointerException
	{
			boolean continueSwitch = true;
			 
			 // if the past view controller was editable, check for unsaved changes  
			if(lastController != null && lastController instanceof EditableView)
			{
				if( ((EditableView) lastController).hasChanged())
				{
					// need to know if user clicked cancel
					continueSwitch = promptSaveAlert();
				}
					
				// user canceled the save operation, cancel view switch 
				if( !continueSwitch )
				{
					return;
				}
					
				((EditableView) lastController).unlockRecord();
			}
		 
			 FXMLLoader loader = new FXMLLoader(parent);
			 
			 loader.setController(controller);
			 
			 Parent parentNode = loader.load();
					
			// make sure borderPane has a reference
			 if(borderPane == null)
			 {
				throw new NullPointerException("BorderPane must have a reference");
			 }
			 
			borderPane.setCenter(parentNode);
			
			lastController = controller;
	}
	
	
	/**
	 * Prompts the user to take an action related to
	 * changes in an EditableView.
	 * 
	 * Options:
	 * 	Yes will save the EditableView's changes,
	 * 	No will not cause any action
	 * 	Cancel will close the prompt
	 * @return true to continue with switching of the view, false otherwise
	 */
	private boolean promptSaveAlert()
	{
		
		boolean leaveView = true;
		
		String contentText = "Would you like to save your changes?";
		
		prompt = new Alert(AlertType.CONFIRMATION, contentText, ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		
		prompt.setHeaderText("Unsaved Changes");
		
		Optional<ButtonType> result = prompt.showAndWait();
		
		// save changes in the dialog
		if(result.isPresent() && result.get() == ButtonType.YES)
		{
			new Runnable() 
					{
						public void run() 
						{
							((EditableView) lastController).save();
						}
					}.run();	
		}
		
		// cancel changes to the dialog
		else if(result.isPresent() && result.get() == ButtonType.CANCEL)
		{
			prompt.close();
			
			leaveView = false;
		}
		
		return leaveView;
	}	

}
