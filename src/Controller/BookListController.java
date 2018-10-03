package Controller;

import View.ViewManager;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
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
	private Button deleteButtonID;
	
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
			
			books = FXCollections.observableList(bookGateway.getBooks());
			
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
		
		deleteButtonID.addEventFilter(MouseEvent.MOUSE_CLICKED, delete);
		
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
	
	/**
	 * Event handler which executes the delete event when clicking the delete button
	 * checks the selected book and calls the gateway delete method
	 * calls constructor/gateway getBooks method to update observable list for the user
	 * 
	 */
	
	EventHandler<MouseEvent> delete = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent e) { 
			   
			   try {
				   
				   if(bookList.getSelectionModel().getSelectedItem() != null)
				   {
					   Book book = bookList.getSelectionModel().getSelectedItem();
					   System.out.println("Book to be deleted: " + bookList.getSelectionModel().getSelectedItem());
					   //Deletes book
					   bookGateway.deleteMethod(book);
					   //Update book list after deletion
					   books = FXCollections.observableArrayList(bookGateway.getBooks());
					   bookList.setItems(books);
				   }
				   
			   } 
			   catch (SQLException exception)
			   {
				   logger.error(String.format("%s: %s", "Couldn't delete book error", exception.getMessage()));
				   
				   Alert alert = new Alert(AlertType.ERROR);
			       alert.setTitle("Deleting Book");
			 
			       alert.setHeaderText(null);
			       String alertmsg = ("SQL delete error: " + exception.getMessage() + ", delete aborted!");
			       alert.setContentText(alertmsg);
			 
			       alert.showAndWait();
			   }

			   logger.info(String.format("%s", "delete button pressed"));
			   
		   } 
	 }; 
		
}
