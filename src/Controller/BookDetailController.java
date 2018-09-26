package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	public BookDetailController(String Title, String Summary, String ISBN, LocalDateTime DateAdded, int YearPublished, String imageName)
	{
		title = Title;
		summary = Summary;
		isbn = ISBN;
		dateAdded = DateAdded;
		yearPublished = YearPublished;
		image = new Image("/View/" + imageName);
	}
	
	/**
	 * Accepts a Book to render in the UI
	 * @param book
	 */
	public BookDetailController(Book book)
	{
		this.book = book;
		
		setBook(this.book);
	}
	
	/**
	 * Used to initialize the BookDetailController
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		titleFieldID.setEditable(false);
		titleFieldID.setText(title);
		
		SummaryFieldID.setEditable(false);
		SummaryFieldID.setText(summary);

		yearPublishedFieldID.setEditable(false);
		
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
		
		isbnFieldID.setEditable(false);
		isbnFieldID.setText(isbn);
		
		dateAddedFieldID.setEditable(false);
		dateAddedFieldID.setText(dateAdded.getMonthValue() + "/" + dateAdded.getDayOfMonth() + "/" + dateAdded.getYear());
		
		imageBoxID.setImage(image);
		
		saveButtonID.addEventFilter(MouseEvent.MOUSE_CLICKED, save);

	}
	
	EventHandler<MouseEvent> save = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent e) { 

			   logger.info(String.format("%s", "Save button pressed"));
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
