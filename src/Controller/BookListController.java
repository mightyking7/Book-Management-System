package Controller;

import View.ViewManager;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

import Database.BookTableGateway;
import Database.DBConnection;
import Model.Book;

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
	private ListView<Book> bookList;			// list of books to render
	
	@FXML
	private BorderPane rootNode;
	
	private ObservableList<Book> books;
	
	private static Logger logger;
	
	private static final int NUM_CLICKS = 2;
	
	private DBConnection conn;
	
	private BookTableGateway bookGateway;
	
	// Cross cutting concern: concept or issue that affects multiple modules of the software
	// dependencies are wrapped all over the place, can be difficult to reason, check, and design
	
	/**
	 * Constructor for logger, creating a DB instance and fetch for all DB books
	 */
	public BookListController()
	{	
		logger = LogManager.getLogger(BookListController.class);
		
		try 
		{
			// obtain instance to the database
			conn = DBConnection.getInstance();
						
			bookGateway = new BookTableGateway(conn.getConnection());
			
			books = FXCollections.observableArrayList(bookGateway.getBooks());
			
		} catch (SQLException e)
		{
			String error = String.format("Could not establish connection to the database %s", e.getMessage());
			logger.error(error);
		}
	}
	
	/**
	 * Used to set the mouse clicked event handler
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		
		bookList.setItems(books);
		
		// The book detail view should open when a list item is double clicked
		
		bookList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			
			@Override
			public void handle(MouseEvent event) 
			{
				Book selectedBook;
				
				ViewManager view;
				
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == NUM_CLICKS)
				{
					try {
						
						selectedBook = bookList.getSelectionModel().getSelectedItem();
						
						// log the event
						logger.info(String.format("%s selected", selectedBook));
						
						// Retrieve the view manager to show details about the book
						URL bookDetails = this.getClass().getResource("/View/BookDetailedView.fxml");
						
						view = ViewManager.getInstance();
						
						view.setCurrentPane(rootNode);
						
						// pass in the selected book model into the controller
						view.switchView(bookDetails, new BookDetailController(selectedBook, bookGateway));
						
					} catch(Exception e)
					{
						logger.error(String.format("%s : %s", this.getClass().getName(), e.getMessage()));
					}
				
				}
			}
		});
	}
		
}
