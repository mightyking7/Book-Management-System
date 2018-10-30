package View;

import java.io.IOException;
import java.net.URL;
import Controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;

/**
 * Singleton responsible for loading a new view into the application border pane
 * Aug 29, 2018
 * @author isaacbuitrago
 */
public class ViewManager 
{
	private static ViewManager instance;
	
	private BorderPane borderPane;
	
	private Controller controller;	// current controller 
	
	private boolean viewEdited;		// flag on if view was edited
	
	private boolean viewSaved;  	// flag on is save action invoked
	
	private Alert prompt;			// prompt to save, exit, or cancel
	
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
	 * @throws IOException If parent url is not valid, null, or could not be loaded
	 * @throws NullPointerException If the managed Layout Pane is null
	 *
	 */
	public void switchView(URL parent, Controller controller ) throws IOException, NullPointerException
	{
		 FXMLLoader loader = new FXMLLoader(parent);
		 
		 // set current controller
		 this.controller = controller;
		 
		 // check if the controller has an editable view
		 checkIfSaved();
		 
		 loader.setController(controller);
		 
		 Parent parentNode = loader.load();
				
		// make sure borderPane has a reference
		if(borderPane == null)
		{
			throw new NullPointerException("BorderPane must have a reference");
		}
		
		borderPane.setCenter(parentNode);
	}
	
	public void checkIfSaved()
	{
		if(controller.hasChanged())
		{
			prompt = new Alert(AlertType.INFORMATION);
			
			prompt.setHeaderText("Unsaved Changes");
			
			prompt.setContentText("Would you like to save your changes?");
			
			prompt.showAndWait();
		}
	}

	public boolean isViewEdited() 
	{
		return viewEdited;
	}

	public void setViewEdited(boolean viewEdited) {
		this.viewEdited = viewEdited;
	}

	public boolean isViewSaved() {
		return viewSaved;
	}

	public void setViewSaved(boolean viewSaved) {
		this.viewSaved = viewSaved;
	}
	
	

}
