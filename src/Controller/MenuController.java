package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import View.ViewManager;

/**
 * @author isaacbuitrago
 * 
 * Controller for the system's main menu
 */
public class MenuController implements Initializable
{
	@FXML
	private BorderPane rootNode;
	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private MenuItem quit;
	
	@FXML
	private MenuItem bookList;
	
	private static Logger logger = LogManager.getLogger(MenuController.class);

	
	/**
	 * Handles menu clicks
	 * @param event
	 */
	@FXML private void handleMenuAction(ActionEvent event) throws IOException
	{	
		if(event.getSource() == quit)
		{
			logger.info("Exiting the system");
			
			System.exit(0);
		}
		else if(event.getSource() == bookList)
		{
			logger.info("Book list selected");
			
			ViewManager manager = ViewManager.getInstance();
			
			// set the root node to manage
			manager.setCurrentPane(rootNode);
			
			try
			{
				URL viewUrl = this.getClass().getResource("/View/BookList.fxml");
				
				manager.switchView(viewUrl , new BookListController());
			} 
			catch(IOException e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
			catch(Exception e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		menuBar.setFocusTraversable(true);
	}

}
