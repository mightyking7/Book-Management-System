package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import Model.Book;
import View.ViewManager;

/**
 * @author isaacbuitrago
 * 
 * Controller for the system's main menu
 */
public class MenuController extends Controller
{	
	@FXML
	private MenuBar menuBar;
	
	@FXML
	private MenuItem homeMenuItem;
	
	@FXML
	private MenuItem quitMenuItem;
	
	@FXML
	private MenuItem bookListMenuItem;
	
	@FXML
	private MenuItem addBookMenuItem;
	
	@FXML
	private ImageView homeImageView;
	
	
	/**
	 * Constructor
	 */
	public MenuController()
	{
		super();
	}
	
	/**
	 * Initializes the menu controller 
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		menuBar.setFocusTraversable(true);
		
		homeImageView.setImage(new Image("/View/books.jpg"));
	}
			
		
	/**
	 * Handles menu clicks
	 * @param event
	 */
	@FXML private void handleMenuAction(ActionEvent event) throws IOException
	{	
		
		URL viewUrl;				// View URL to load for a menu item
		
		if(event.getSource() ==  homeMenuItem)
		{
//			viewManager = ViewManager.getInstance();
//			
//			// set the root node to manage
//			viewManager.setCurrentPane(rootNode);
//			
//			try
//			{
//				viewUrl = this.getClass().getResource("/View/menu.fxml");
//				
//				viewManager.switchView(viewUrl , new MenuController());
//			} 
//			catch(IOException e)
//			{
//				logger.error(this.getClass().getName() + ":" + e.getMessage());
//			}
//			catch(NullPointerException e)
//			{
//				logger.error(this.getClass().getName()+ ":" + e.getMessage());
//			}
//			catch(Exception e)
//			{
//				logger.error(this.getClass().getName() + ":" + e.getMessage());
//			}
		}
		else if(event.getSource() == quitMenuItem)
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
				
				ObservableList<Book> books  = FXCollections.observableList(bookTableGateway.getBooks());
				
				viewManager.switchView(viewUrl , new BookListController(books));
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
				
				viewManager.switchView(viewUrl, new BookDetailController(new Book()));
				
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
