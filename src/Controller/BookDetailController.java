package Controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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


public class BookDetailController implements Initializable {
	
	private static Logger logger = LogManager.getLogger(BookDetailController.class);
	
	private String title;
	
	private String summary;
	
	private String isbn;
	
	private LocalDateTime dateAdded;
	
	private int yearPublished;
	
	private Image image;
	
	private Book book;		// the book to report on
	
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
	 * Second constructor for manually displaying a book
	 */
	
	public BookDetailController(String Title, String Summary, String ISBN, LocalDateTime DateAdded, int YearPublished, BookTableGateway bookGateway)
	{
		this.bookGateway = bookGateway;
		this.title = Title;
		this.summary = Summary;
		this.isbn = ISBN;
		this.dateAdded = DateAdded;
		this.yearPublished = YearPublished;
		this.image = new Image("/View/" + "Book-2.png");
	}
	
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
		
		setBook(this.book);
	}
	
	/**
	 * Used to initialize the BookDetailController
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		titleFieldID.setEditable(true);
		titleFieldID.setText(title);
		
		SummaryFieldID.setEditable(true);
		SummaryFieldID.setText(summary);

		yearPublishedFieldID.setEditable(true);
		
		if(yearPublished < 1900 || yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR))
		{
			
			yearPublishedFieldID.setText("Year error found");
			logger.error(String.format("Year input: Outside of valid year-range"));
		}
		else
		{
			String displayYear = "" + yearPublished;
			yearPublishedFieldID.setText(displayYear);	
		}
		
		isbnFieldID.setEditable(true);
		isbnFieldID.setText(isbn);
		
		dateAddedFieldID.setEditable(false);
		dateAddedFieldID.setText(dateAdded.getMonthValue() + "/" + dateAdded.getDayOfMonth() + "/" + dateAdded.getYear());
		
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
	  *  Binds the GUI's controls to the current book
	  */
	 private void setBook(Book book)
	 {
		 
		 this.title = book.getTitle();
		 
		 this.summary = book.getSummary();
		 
		 this.yearPublished = book.getYearPublished();
		 
		 this.isbn = book.getIsbn();
		 
		 this.dateAdded = book.getDateAdded();
		 
	 }

}
