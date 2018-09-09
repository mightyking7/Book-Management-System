package Controller;

import View.ViewManager;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.time.Month;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Sep 1, 2018
 * 
 * Responsible for populating BookList view with book titles and handling intents to
 * display details about a Book.
 * 
 *@author isaacbuitrago
 */

public class BookListController implements Initializable
{

	@FXML
	private ListView<String> bookList;
	
	@FXML
	private BorderPane rootNode;
	
	private ViewManager view;
	
	private static ObservableList<String> bookTitles  = FXCollections.observableArrayList();
	
	private static Logger logger = LogManager.getLogger(BookListController.class);
	
	private static final int NUM_CLICKS = 2;
	
	/**
	 * Constructor
	 */
	public BookListController()
	{
		bookTitles.addAll("Lone Survivor", "Brothers Kamarozov", "War and Peace", "Animal Farm");
	}
	
	/**
	 * Used to set the mouse clicked event handler
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		bookList.setItems(bookTitles);
		
		// The book detail view should open when a list item is double clicked
		
		bookList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) 
			{
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == NUM_CLICKS)
				{
					try {
						// log the event
						logger.info(String.format("%s selected", bookList.getSelectionModel().getSelectedItem()));
						
						// Retrieve the view manager to show details about the book
						URL bookDetails = this.getClass().getResource("/View/BookDetailedView.fxml");
						
						view = ViewManager.getInstance();
						
						view.setCurrentPane(rootNode);
						
						view.switchView(bookDetails, new BookDetailController( bookList.getSelectionModel().getSelectedItem(),"Random text" + System.lineSeparator() + "More Random Text", "42397fs98f", LocalDate.of( 2015 , Month.FEBRUARY , 18), 2015, "Book-2.png"));
						
					} catch(Exception e)
					{
						logger.error(String.format("%s : %s", this.getClass().getName(), e.getMessage()));
					}
				
				}
			}
		});
	}
		
}
