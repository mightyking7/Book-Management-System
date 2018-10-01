package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import Database.BookTableGateway;
import Model.Book;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BookDetailController implements Initializable 
{
	
	private static Logger logger;
	
	private Image image;
	
	private Book book;		// the book to display
	
	private BookTableGateway bookGateway;
	
	@FXML
	private TextField titleFieldID;
	
	@FXML
	private TextArea SummaryFieldID;
	
	@FXML
	private TextField yearPublishedFieldID;
	
	@FXML
	private TextField isbnFieldID;
	
	@FXML
	private TextField dateAddedFieldID;
	
	@FXML
	private Button saveButtonID;
	
	@FXML
	private ImageView imageBoxID;
	
	/**
	 * Accepts a Book to render in the UI
	 * Also gets an instance of the bookGateway
	 * @param book
	 */
	public BookDetailController(Book book, BookTableGateway bookGateway)
	{
		this.image = new Image("/View/" + "Book-2.png");
		
		this.bookGateway = bookGateway;
		 
		this.book = book;
	}
	
	/**
	 * Used to initialize the BookDetailController
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		logger = LogManager.getLogger(BookDetailController.class);
		
		setBookDetails(this.book);
		
		imageBoxID.setImage(image);
		
		saveButtonID.addEventFilter(MouseEvent.MOUSE_CLICKED, save);

	}
	
	/**
	 * Save button used to update a book in the database and local memory by updating the book object and calling the UpdateBook function
	 * Catches and displays any exception thrown from the model
	 */
	EventHandler<MouseEvent> save = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent e) { 

			   logger.info(String.format("%s", "Save button pressed"));
			   
			   try
			   {
				   book.setTitle(titleFieldID.getText());
				   book.setSummary(SummaryFieldID.getText());
				   book.setYearPublished(Integer.parseInt(yearPublishedFieldID.getText()));
				   book.setIsbn(isbnFieldID.getText());
				   book.save(bookGateway.UpdateBook(book));
			   }
			   catch (SQLException exception)
			   {
				   logger.error(String.format("%s: %s, Save aborted!", "SQL save error",exception.getMessage()));   
			   }
			   catch (Exception exception)
			   {
				   logger.error(String.format("%s: %s, Save aborted!", "Save error",exception.getMessage()));   
			   }
		   } 
	 }; 
	 
	 /**
	  *  Sets the BookDetailView's controls to the attributes of the given book
	  *  
	  *  @param book to render details about
	  */
	 private void setBookDetails(Book book)
	 {
		 LocalDateTime dateAdded = book.getDateAdded();
		 
		 int yearPublished = book.getYearPublished();
		 
		 // retrieve the book's details from the model
		 
		 String titleText = book.getTitle();		
		 
		 String summaryText = book.getSummary();
		 
		 String yearPublishedText = String.valueOf(yearPublished);
		 
		 String isbnText = book.getIsbn();
		 
		 String dateAddedText = dateAdded.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
		 
		 if(yearPublished < 1900 || yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR))
		 {
			 yearPublishedFieldID.setText("Year error found");
			 
			logger.error(String.format("Year input: Outside of valid year-range"));		
		 }
		 else
		 {
			yearPublishedFieldID.setText(yearPublishedText);	
		 }
		 
		 this.titleFieldID.setText(titleText);
		 
		 this.SummaryFieldID.setText(summaryText);
		 
		 this.isbnFieldID.setText(isbnText);
		 
		 this.dateAddedFieldID.setText(dateAddedText);
	 }

}
