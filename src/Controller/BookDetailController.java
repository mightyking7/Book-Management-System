package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import Model.Book;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BookDetailController extends Controller
{	
	private Image image;
	
	private Book book;
	
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
	public BookDetailController(Book book)
	{
		super();
		
		this.image = new Image("/View/" + "Book-2.png");
		 
		this.book = book;
	}
	
	/**
	 * Used to initialize the BookDetailController
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		logger = LogManager.getLogger(BookDetailController.class);
		
		// set GUI field values to appropriate model field values
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
				   // set the gateway for the model
				   book.setGateway(bookTableGateway);
				   
				   // set the current book values
				   book.setTitle(titleFieldID.getText());
				   book.setSummary(SummaryFieldID.getText());
				   book.setYearPublished(Integer.parseInt(yearPublishedFieldID.getText()));
				   book.setIsbn(isbnFieldID.getText());
				   
				   // save the book
				   book.save();
				   
				   logger.info(String.format("%s saved to the database", book.getTitle()));
				   
			       infoAlert.setTitle("Saving Book");
			 
			       infoAlert.setHeaderText(null);
			       
			       infoAlert.setContentText("Book was saved successfully!");
			 
			       infoAlert.showAndWait();
				   
			   }
			   catch (SQLException exception)
			   {
				   logger.error(String.format("%s: %s, Save aborted!", "SQL save error",exception.getMessage()));   
				   
			       errorAlert.setTitle("Saving Book");
			 
			       errorAlert.setHeaderText(null);
			       
			       String alertmsg = ("SQL save error: " + exception.getMessage() + ", Save aborted!");
			       
			       errorAlert.setContentText(alertmsg);
			 
			       errorAlert.showAndWait();
			   }
			   catch (Exception exception)
			   {
				   logger.error(String.format("%s: %s, Save aborted!", "Save error",exception.getMessage()));   
				   
			       errorAlert.setTitle("Saving Book");
			 
			       errorAlert.setHeaderText(null);
			       
			       String errormsg = ("Unexpected save error: " + exception.getMessage() + ", Save aborted!");
			       
			       errorAlert.setContentText(errormsg);
			 
			       errorAlert.showAndWait();
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
		 
		 if(dateAdded == null)
		 {
			 //if dateAdded is null then user is adding a new book by business rules so no error needs to be displayed
		 }
		 else if(yearPublished < 1900 || yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR))
		 {
			yearPublishedFieldID.setText("Please input a valid year");
			 
			logger.error(String.format("Year input: Outside of valid year-range"));		
		 }
		 else
		 {
			yearPublishedFieldID.setText(yearPublishedText);	
		 }
		 
		 if(dateAdded != null)
		 {
			 String dateAddedText = dateAdded.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
			 
			 this.dateAddedFieldID.setText(dateAddedText);
		 }
		 else
		 {
			 //Displays the current date if a new book is being inserted
			 this.dateAddedFieldID.setText(new SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis()));
		 }
		 
		 this.titleFieldID.setText(titleText);
		 
		 this.SummaryFieldID.setText(summaryText);
		 
		 this.isbnFieldID.setText(isbnText);
		 
	 }

}
