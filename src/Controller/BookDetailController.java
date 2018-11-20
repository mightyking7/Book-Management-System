package Controller;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import Database.PublisherTableGateway;
import Model.Author;
import Model.AuthorBook;
import Model.Book;
import Model.Publisher;
import View.ViewManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
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
	
	private Publisher selectedPublisher;
	
	@FXML
	private TableView authorBookTable;
	
	@FXML
	private TableColumn<Author, String> authorColumn;
	
	@FXML
	private TableColumn<AuthorBook, Integer> royaltyColumn;
	
	private ObservableList<AuthorBook> tableData;
	
	@FXML
	private Button AddAuthorid;
	
	@FXML
	private Button DeleteAuthorid;
	
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
	void ComboBoxSelected(ActionEvent event) 
	{
		selectedPublisher = comboBoxID.getSelectionModel().getSelectedItem();
		
		logger.info(String.format("Publisher %s selected for %s", selectedPublisher, book.getTitle()));
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
		
		// Loads the Publishers in to the comboBox and sets the selectedPublisher
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
		
		// disable the viewAuditTrailButton for new Books
		
		if(book.getId() == 0)
		{
			ViewAuditTrailButton.setDisable(true);
		}
		
		saveButtonID.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> save());
		
		initializeAuthorBookTable(this.book);
		
		//AuthorListid.addEventFilter(MouseEvent.MOUSE_CLICKED, selectAuthor);
		
		
	}
	
	EventHandler<MouseEvent> selectAuthor = new EventHandler<MouseEvent>()
	{
		/**
		 * handles selection of Authors
		 */
		@Override
		public void handle(MouseEvent event) 
		{
			AuthorBook selectedAuthor;
			
			if(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2)
			{
//				try 
//				{
//					selectedAuthor = AuthorListid.getSelectionModel().getSelectedItem();
//					
//					// log the event
//					logger.info(String.format("%s selected", selectedAuthor));
//					
//					UpdateAuthor(selectedAuthor);
//					
//				} catch(Exception e)
//				{
//					logger.error(String.format("%s : %s", this.getClass().getName(), e.getMessage()));
//				}
			}
		}
		
	};
	
	public void UpdateAuthor(AuthorBook authorBook) 
	{
		Dialog<Object> updateAuthor = new Dialog<>();
		
		updateAuthor.setTitle("Update Author");
		
		updateAuthor.setHeaderText("Please Update required Author fields:");
		
        DialogPane dialogPane = updateAuthor.getDialogPane();
        
        dialogPane.getButtonTypes().addAll(ButtonType.APPLY, ButtonType.CANCEL);
        
        TextField firstName = new TextField(authorBook.getAuthor().getFirstName());
        TextField lastName = new TextField(authorBook.getAuthor().getLastName());
        TextField royalty = new TextField(String.valueOf(authorBook.getRoyalty()));
        
        updateAuthor.getDialogPane().setMinSize(400, 200);
        
        dialogPane.setContent(new VBox(8, firstName,lastName,royalty));
       
        updateAuthor.setResultConverter(button -> button == ButtonType.APPLY);

        updateAuthor.showAndWait().ifPresent(bool -> {
    		
    		if(bool.toString() == "true")
    		{
            	authorBook.getAuthor().setFirstName(firstName.getText());
        		authorBook.getAuthor().setLastName(lastName.getText());
        		authorBook.setRoyalty(Integer.parseInt(royalty.getText()));
        		
        		//this.AuthorListid.refresh();
    		}
            else
            {
            	
            }
    		
    	});
    }
	
	public void initializeAuthorBookTable(Book book)
	{	
		try 
		{
			// set data properties to columns
			authorColumn.setCellValueFactory( new PropertyValueFactory<Author, String>("author"));
			
			royaltyColumn.setCellValueFactory( new PropertyValueFactory<AuthorBook, Integer>("royalty"));
			
			// set the the table data
			tableData = FXCollections.observableArrayList(book.getAuthors(book.getId(), bookTableGateway));
			
			authorBookTable.setItems(tableData);
			
		} catch (Exception e) {
			logger.error(String.format("%s (In loadAuthorList method)", e.getMessage()));
		}
	}
	
	/**
	 * Save button used to update a book in the database and local 
	 * memory by updating the book object and calling the UpdateBook function
	 * Catches and displays any exception thrown from the model
	 */
	@Override
	public void save()
	{
		   // no action for empty books
		   if(! hasChanged())
		   {
			   return;
		   }
		   
		   String bookTitle = titleFieldID.getText();
		   
		   String summary = SummaryFieldID.getText();
		   
		   int yearPublished = (yearPublishedFieldID.getText().isEmpty() ? 0 : Integer.parseInt(yearPublishedFieldID.getText()));
		   
		   String isbn = isbnFieldID.getText();

		   logger.info(String.format("%s", "Save button pressed"));
		   
		   try
		   {
			   // set the gateway for the model
			   book.setGateway(bookTableGateway);
			   
			   // validate user input
			   
			   if( bookTitle != null && !(bookTitle.length() >= 1 && bookTitle.length() <= 255))
			   {
				   String errorMessage = String.format("Title cannot be longer than %d characters, "
				   		+ "it is %d characters", 255, bookTitle.length());
				   
				   throw new Exception(errorMessage);
			   }
			   else if( summary != null && !(summary.length() <= 65536))
			   {
				   String errorMessage = String.format("Summary cannot be longer than %d characters, "
					   		+ "it is %d characters", 65536, summary.length());
				   
					   throw new Exception(errorMessage);
			   }
			   else if(yearPublished > (int)Calendar.getInstance().get(Calendar.YEAR) || yearPublished < 1900)
			   {
				   int currentYear = (int)Calendar.getInstance().get(Calendar.YEAR);
				   
				   String errorMessage = String.format("Year published must be between 1900 and %d, ", currentYear);
				   
					   throw new Exception(errorMessage);
			   }
			   else if(isbn != null && !(isbn.length() <= 13))
			   {
				   String errorMessage = String.format("The book Isbn cannot be longer than %d characters, "
				   		+ "it is currently %d characters", 13, isbn.length());
				   
				   throw new Exception(errorMessage);
			   }
			   
			   // copy values to original model for existing books only
			   book.updateBookModel(bookTitle,summary,yearPublished,isbn, selectedPublisher);
			   
			   // save the book
			   book.save();
			   
			   if(ViewAuditTrailButton.isDisable())
			   {
				   ViewAuditTrailButton.setDisable(false);
			   }
			   
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
						//locks the view audit button if the book is not created yet
						if(book.getId() == 0)
						{
							logger.info(this.getClass().getName() + ": Audit trail disabled when adding new books");
						}
						else
						{
							book.setGateway(bookTableGateway);
							URL viewUrl = this.getClass().getResource("/View/AuditTrailView.fxml");
							viewManager.switchView(viewUrl, new AuditTrailController(book));
						}
						
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
		 try 
		 {
			int selectedPubIndex = PublisherTableGateway.DEFAULT_PUBLISHER_ID;
			 
			this.publisherTableGateway = new PublisherTableGateway();
				
			ObservableList<Publisher> publishers = FXCollections.observableArrayList(publisherTableGateway.fetchPublishers());
				
			comboBoxID.setItems(publishers);
			
			// If not a new book, get the publisher for the book
			
			if( book.getPublisher() != null)
			{
				selectedPubIndex = book.getPublisher().getId();
			}
			
			// ObservableList of Publishers is zero indexed
			comboBoxID.getSelectionModel().select(selectedPubIndex - 1 );
			
			// set the selected Publisher
			selectedPublisher = comboBoxID.getSelectionModel().getSelectedItem();
				
		 } catch (SQLException e) 
		 {
				logger.error(e.getMessage());
		 } catch (Exception e) 
		 {
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

		if(! (Objects.equals(book.getTitle(), titleFieldID.getText())) )
			edited = true;
		
		if(! (Objects.equals(book.getSummary(), SummaryFieldID.getText())))
			edited = true;
		
		if(book.getYearPublished() != ( yearPublishedFieldID.getText().isEmpty() ? 0 :
										Integer.parseInt(yearPublishedFieldID.getText())) )
			edited = true;
		
		if(! (Objects.equals(book.getIsbn(), isbnFieldID.getText())) )
			edited = true;	
		
		if(! Objects.equals(selectedPublisher.getId() , book.getPublisher() == null ? 1 :
														book.getPublisher().getId()))
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
