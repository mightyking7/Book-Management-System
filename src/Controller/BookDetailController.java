package Controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * 
 * @author HercHja
 * Gets the book details input from the BookListController through its constructor 
 * and uses the BookDetailedView view to display the bookDetailedView GUI
 */


public class BookDetailController implements Initializable {
	
	private static Logger logger = LogManager.getLogger(BookDetailController.class);
	
	private String title;
	private String summary;
	private String isbn;
	private LocalDate dateAdded;
	private int yearPublished;
	private Image image;
	
	public BookDetailController(String Title, String Summary, String ISBN, LocalDate DateAdded, int YearPublished, String imageName)
	{
		
		title = Title;
		summary = Summary;
		isbn = ISBN;
		dateAdded = DateAdded;
		yearPublished = YearPublished;
		image = new Image("/View/" + imageName);
	}
	
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
	 * Sets up GUI and checks some input logic for errors
	 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		titleFieldID.setEditable(false);
		titleFieldID.setText(title);
		
		SummaryFieldID.setEditable(false);
		SummaryFieldID.setText(summary);

		yearPublishedFieldID.setEditable(false);
		
		if(yearPublished < 1900 || yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR))
		{
			
			yearPublishedFieldID.setText("Year error found");
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
	
	/**
	 *  EventHandler: Handles the mouse click events on the save button
	 * 
	 */
	
	EventHandler<MouseEvent> save = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent e) { 

			   logger.info(String.format("%s : %s", this.getClass().getName(), "Save button pressed"));
		   } 
	 };   

}
