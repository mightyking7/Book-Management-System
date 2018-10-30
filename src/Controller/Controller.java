package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Database.BookTableGateway;
import Database.DBConnection;
import View.ViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

/**
 * Abstract Controller that contains resources, behavior,
 * and attributes needed by all controllers.
 * 
 * @author isaacbuitrago
 */
public abstract class Controller implements Initializable 
{
	
	protected Alert errorAlert;						// Alert to display error messages
	
	protected Alert infoAlert;						// Alert to display info messages
	
	protected Alert warningAlert;					// Alert to display warning messages
	
	protected BookTableGateway bookTableGateway;	// reference to BookTableGateway
	
	protected ViewManager viewManager;				// ViewManager to change views
	
	protected static Logger logger;					// logger for a Controller
	
	@FXML
	protected BorderPane rootNode;					// root pane for the controlled scene
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		// Subclasses should Override as necessary
	}
	
	/**
	 * Constructs a Controller with alerts, a logger, and a BookTableGateway
	 */
	public Controller()
	{
		errorAlert = new Alert(AlertType.ERROR);
		
		warningAlert = new Alert(AlertType.WARNING);
		
		infoAlert = new Alert(AlertType.INFORMATION);
		
		// set the Logger based on the concrete runtime object
		
		logger = LogManager.getLogger(this.getClass());
		
		// establish  BookTableGateway and handle the error
		try
		{
			bookTableGateway = new BookTableGateway();
		}
		catch(SQLException e)
		{
			handleDatabaseError(e);
			
			try
			{
				DBConnection.getInstance().getConnection().close();
				
			} catch (SQLException e2)
			{
				
				handleDatabaseError(e2);
			}
		}
	}
	
	/**
	 * Determines if an editable view has changed
	 * @return
	 */
	public boolean hasChanged()
	{
		return false;
	}
	
	/**
	 * Handles error when connecting or interacting with the database.
	 * 
	 * @param e exception returned from a Gateway
	 */
	public void handleDatabaseError(Exception e)
	{
		String errorMessage = String.format("%s.\n%s", 
				"There was an issue with the database", e.getMessage());
		
		// log the error
		logger.error(errorMessage);
		
		// show the Alert
		errorAlert.setTitle("Error");
		
		errorAlert.setHeaderText("Database Error");
		
	    errorAlert.setContentText(errorMessage);
	    
	    errorAlert.showAndWait();
		
	}
	

}
