package View;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.*;

/*
 * Responsible for loading a new view into the application border pane
 * Aug 29, 2018
 * isaacbuitrago
 */

public class ViewManager 
{
	
	private static ViewManager instance;
	
	private BorderPane borderPane;
	
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
	 * 
	 * @param currentPane
	 */
	public void setCurrentPane(BorderPane currentPane) 
	{
		this.borderPane = currentPane;
	}
	
	/**
	 * Used to change the view of the current Pane 
	 * 
	 * @param parent Relative URL of the of the new view to load
	 * @throws IOException if parent is null
	 * @throws IOException if the parent is not a valid URL or was null
	 */
	public void switchView(String parent) throws IOException, NullPointerException
	{
		 URL parentUrl = this.getClass().getResource(parent);
		 
		 FXMLLoader loader = new FXMLLoader(parentUrl);
		 
		 Parent parentNode = loader.load();
				
		// make sure borderPane has a reference
		if(borderPane == null)
		{
			throw new NullPointerException("BorderPane must have a reference");
		}
		
		borderPane.setCenter(parentNode);
	}


}
