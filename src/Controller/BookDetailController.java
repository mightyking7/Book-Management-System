package Controller;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;

import Database.PublisherTableGateway;
import Model.AuditTrailEntry;
import Model.Book;
import Model.Publisher;
import View.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Responsible for rendering book details, saving book detail changes,
 * and delegating responsibility to display the audit trail details for a book.
 * 
 * @author isaacbuitrago
 */
public class BookDetailController extends Controller implements EditableView
{	
	private Image image;
	
	private Book book;
	
	private PublisherTableGateway publisherTableGateway;
	
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
	private Button ViewAuditTrailButton;
	
	@FXML
	private ImageView imageBoxID;
	
	@FXML
	private ComboBox<Publisher> comboBoxID;
	
	@FXML
	void ComboBoxSelected(ActionEvent event) {

	    try {
	    	publisherTableGateway.updatePublisherIDInBooksTable(this.book, comboBoxID.getSelectionModel().getSelectedItem().getId());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
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
		
		// Loads the Publishers in to the comboBox
		getPublisherList();
		
		// set GUI field values to appropriate model field values
		setBookDetails(this.book);
		
		// set the image of the book
		imageBoxID.setImage(this.image);
		
		// lock existing books for update
		if(book.getId() > 0)
		{
			lockRecord();
		}
	
		// add the event handler for the audit trail button
		ViewAuditTrailButton.addEventFilter(MouseEvent.MOUSE_CLICKED, viewAuditTrail);
		
		saveButtonID.addEventFilter(MouseEvent.MOUSE_CLICKED, save);
	}
	
	/**
	 * Event handler for the viewAuditTrail button when viewing a book
	 */
	
	EventHandler<MouseEvent> viewAuditTrail = new EventHandler<MouseEvent>() { 
		   @Override 
		   public void handle(MouseEvent e) { 

				   //Gets the viewManager instance and sets this pane to be the current viewManage pane
				   viewManager = ViewManager.getInstance();
				   viewManager.setCurrentPane(viewManager.getCurrentPane());
					
				   //Gets the new view to be displayed and passes objects through the AuditTrailController controller constructor
					try 
					{
						book.setGateway(bookTableGateway);
						URL viewUrl = this.getClass().getResource("/View/AuditTrailView.fxml");
						viewManager.switchView(viewUrl, new AuditTrailController(book));
						
					} catch(IOException et)
					{
						logger.error(this.getClass().getName() + ":" + et.getMessage());
					}
					catch(NullPointerException et)
					{
						logger.error(this.getClass().getName()+ ":" + et.getMessage());
					}
					catch(Exception et)
					{
						logger.error(this.getClass().getName() + ":" + et.getMessage());
					}
		
		   }
		   
	};
	
	/**
	 * Save button used to update a book in the database and local memory by updating the book object and calling the UpdateBook function
	 * Catches and displays any exception thrown from the model
	 */
	EventHandler<MouseEvent> save = new EventHandler<MouseEvent>() 
	{ 
		   @Override 
		   public void handle(MouseEvent e)
		   {    
			   String bookTitle = titleFieldID.getText();
			   
			   String summary = SummaryFieldID.getText();
			   
			   int yearPublished = Integer.parseInt(yearPublishedFieldID.getText());
			   
			   String isbn = isbnFieldID.getText();

			   logger.info(String.format("%s", "Save button pressed"));
			   
			   try
			   {
				   
				   // set the gateway for the model
				   book.setGateway(bookTableGateway);
				   
				   // validate user input
				   
				   if(!(bookTitle.length() >= 1 && bookTitle.length() <= 255))
				   {
					   String errorMessage = String.format("Title cannot be longer than %d characters, "
					   		+ "it is %d characters", 255, bookTitle.length());
					   
					   throw new Exception(errorMessage);
				   }
				   else if( !(summary.length() <= 65536))
				   {
					   String errorMessage = String.format("Summary cannot be longer than %d characters, "
						   		+ "it is %d characters", 65536, summary.length());
					   
						   throw new Exception(errorMessage);
				   }
				   else if(yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR))
				   {
					   int currentYear = (int)Calendar.getInstance().get(Calendar.YEAR);
					   
					   String errorMessage = String.format("Year published cannot be after %d, ", currentYear);
					   
						   throw new Exception(errorMessage);
				   }
				   else if(!(isbn.length() <= 13))
				   {
					   String errorMessage = String.format("The book Isbn cannot be longer than %d characters, "
					   		+ "it is currently %d characters", 13, isbn.length());
					   
					   throw new Exception(errorMessage);
				   }
				   
				   // copy values to original model
				   book.updateBookModel(bookTitle,summary,yearPublished,isbn);
				   
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
	 
	 private void getPublisherList()
	 {
		 try {
				this.publisherTableGateway = new PublisherTableGateway();
				comboBoxID.setItems(publisherTableGateway.fetchPublishers());
				comboBoxID.getSelectionModel().select(publisherTableGateway.getBookAndPublisherConnection(this.book));
			} catch (SQLException e) {
				logger.error(e.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
	 }
	 
	 /**
	  * Compare the book's attributes to the data in the Dialog
	  */
	 @Override
	public boolean hasChanged() 
	 {
		boolean edited = false;
		
		if(! (book.getTitle().equals(titleFieldID.getText())))
			edited = true;
		
		if(! (book.getSummary().equals(SummaryFieldID.getText())))
			edited = true;
		
		if(book.getYearPublished() != Integer.parseInt(yearPublishedFieldID.getText()))
			edited = true;
		
		if(! (book.getIsbn().equals(isbnFieldID.getText())))
			edited = true;		
		
		return edited;
	}

	/**
	  * Locks a book object's Database record
	  * by initiating a transaction with the current
	  * book selected for update.
	  */
	 @Override
	 public void lockRecord()
	 {
		 try 
		 { 
			bookTableGateway.lockBook(this.book);
			
		 }catch (SQLException e) 
		 {
			handleDatabaseError(e);
		 }
		 
	 }

	 /**
	  * Unlocks a book object's Database 
	  * record by terminating the current transaction
	  */
	@Override
	public void unlockRecord() 
	{
		try 
		{
			bookTableGateway.unlockBook(book);
			
		} catch (SQLException e) 
		{
			handleDatabaseError(e);
		}
		
	}

}
