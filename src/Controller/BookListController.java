package Controller;

import View.ViewManager;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import Model.Book;

/**
 * Sep 1, 2018
 * 
 * Responsible for populating BookList view with book titles and handling intents to
 * display details about a Book.
 * 
 *@author isaacbuitrago
 */

public class BookListController extends Controller
{

	@FXML
	private Button deleteButtonID;		// button to delete a book
	
	@FXML
	private ListView<Book> bookList;	// list of books from Database
	
	private ObservableList<Book> books;	// list of books to render
	
	private static final int NUM_CLICKS = 2;	// click count for selecting book

	
	// Cross cutting concern: concept or issue that affects multiple modules of the software
	// dependencies are wrapped all over the place, can be difficult to reason, check, and design
	
	/**
	 * Constructor for logger, creating a DB instance and fetch for all DB books
	 * 
	 * @param books ObservableList of books to bind to a ListView
	 */
	public BookListController(ObservableList<Book> books)
	{	
		super();
		
		this.books = books;
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
		bookList.setOnMouseClicked(new EventHandler<MouseEvent>()
		{	
			/**
			 * 
			 */
			@Override
			public void handle(MouseEvent event) 
			{
				Book selectedBook;
				
				if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == NUM_CLICKS)
				{
					try 
					{
						selectedBook = bookList.getSelectionModel().getSelectedItem();
						
						// log the event
						logger.info(String.format("%s selected", selectedBook));
						
						// Retrieve the view manager to show details about the book
						URL bookDetails = this.getClass().getResource("/View/BookDetailedView.fxml");
						
						viewManager = ViewManager.getInstance();
						
						viewManager.setCurrentPane(rootNode);
						
						// pass in the selected book model into the controller
						viewManager.switchView(bookDetails, new BookDetailController(selectedBook));
						
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
					   bookTableGateway.deleteMethod(book);
					   //Update book list after deletion
					   books = FXCollections.observableArrayList(bookTableGateway.getBooks());
					   bookList.setItems(books);
				   }
				   
			   } 
			   catch (SQLException exception)
			   {
				   logger.error(String.format("%s: %s", "Couldn't delete book error", exception.getMessage()));
				   
			       errorAlert.setTitle("Deleting Book");
			 
			       errorAlert.setHeaderText(null);
			       
			       String alertmsg = ("SQL delete error: " + exception.getMessage() + ", delete aborted!");
			       
			       errorAlert.setContentText(alertmsg);
			 
			       errorAlert.showAndWait();
			   }

			   logger.info(String.format("%s", "delete button pressed"));
			   
		   } 
	 }; 
		
}
