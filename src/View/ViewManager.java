package View;

import java.io.IOException;
import java.net.URL;
import Controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
		 
		 loader.setController(controller);
		 
		 Parent parentNode = loader.load();
				
		// make sure borderPane has a reference
		if(borderPane == null)
		{
			throw new NullPointerException("BorderPane must have a reference");
		}
		
		borderPane.setCenter(parentNode);
	}


}
