package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import Database.BookTableGateway;
import Model.Book;
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
	private MenuItem quitMenuItem;
	
	@FXML
	private MenuItem bookListMenuItem;
	
	@FXML
	private MenuItem addBookMenuItem;
	
	private static Logger logger = LogManager.getLogger(MenuController.class);
	
	/**
	 * Initializes the menu controller 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		menuBar.setFocusTraversable(true);
	}
			
		
	/**
	 * Handles menu clicks
	 * @param event
	 */
	@FXML private void handleMenuAction(ActionEvent event) throws IOException
	{	
		
		URL viewUrl;				// View URL to load for a menu item
		
		ViewManager viewManager;	// ViewManager instance to switch views
		
		if(event.getSource() == quitMenuItem)
		{
			logger.info("Exiting the system");
			
			System.exit(0);
		}
		else if(event.getSource() == bookListMenuItem)
		{	
			viewManager = ViewManager.getInstance();
			
			// set the root node to manage
			viewManager.setCurrentPane(rootNode);
			
			try
			{
				viewUrl = this.getClass().getResource("/View/BookListView.fxml");
				
				viewManager.switchView(viewUrl , new BookListController());
			} 
			catch(IOException e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
			catch(NullPointerException e)
			{
				logger.error(this.getClass().getName()+ ":" + e.getMessage());
			}
			catch(Exception e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
		}
		else if(event.getSource() == addBookMenuItem)
		{
			viewManager = ViewManager.getInstance();
			
			// set the root node to manage
			viewManager.setCurrentPane(rootNode);
			
			try 
			{
				viewUrl = this.getClass().getResource("/View/BookDetailedView.fxml");
				
				BookTableGateway gateway = new BookTableGateway();
				
				viewManager.switchView(viewUrl, new BookDetailController(new Book(), gateway));
				
			} catch(IOException e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
			catch(NullPointerException e)
			{
				logger.error(this.getClass().getName()+ ":" + e.getMessage());
			}
			catch(Exception e)
			{
				logger.error(this.getClass().getName() + ":" + e.getMessage());
			}
		}
	}
	
	

}
