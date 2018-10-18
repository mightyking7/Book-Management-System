package Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.AuditTrailEntry;
import Model.Book;
import View.ViewManager;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * AuditTrail Controller which handles any interaction between the AuditTrail view and the AuditTrail model as well as
 * going back to the previous view
 * @author HercHja
 *
 */

public class AuditTrailController extends Controller {

	private ObservableList<AuditTrailEntry> models;
	private Book book;
	
	@FXML
	private TextField bookTitle;
	
	@FXML
	private ListView<AuditTrailEntry> AuditTrailModelList;
	
	@FXML
	private Button BackButton;
	
	// Constructor which takes a book object and a list of AuditTrailEntry models to display in the AuditTrailEntry View
	
	public AuditTrailController(Book book, ObservableList<AuditTrailEntry> models)
	{
		this.models = models;
		this.book = book;
	}
	
	//JavaFx initializer which sets the title of the book in the AuditTrail View and also initializes the AuditlistView in the View
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		String auditTrailTitle = "Audit Trail for " + this.book.getTitle();
		this.bookTitle.setText(auditTrailTitle);
		
		this.AuditTrailModelList.setItems(models);
		
		BackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, back);
	}
	
	//Back button event handler which handles the click event when going back to the previous view
	
	EventHandler<MouseEvent> back = new EventHandler<MouseEvent>() { 

		@Override 
		   public void handle(MouseEvent e) 
		   { 
			   
				viewManager = ViewManager.getInstance();
				viewManager.setCurrentPane(viewManager.getCurrentPane());
				URL viewUrl = this.getClass().getResource("/View/BookDetailedView.fxml");
				
				try {
					viewManager.switchView(viewUrl,new BookDetailController(book));
				} catch (NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		   
		   }
	
	};
	
}
