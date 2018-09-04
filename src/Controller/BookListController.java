package Controller;

import View.ViewManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private ListView<String> bookList;
	
	@FXML
	private BorderPane rootNode;
	
	private ViewManager view;
	
	private static ObservableList<String> bookTitles  = FXCollections.observableArrayList();
	
	private static Logger logger = LogManager.getLogger(BookListController.class);
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		bookTitles.addAll("Lone Survivor", "Brothers Kamarozov", "War and Peace", "Animal Farm");
		
		bookList.setItems(bookTitles);
		
		// add listener to respond to selection changes and display the BookDetailView
		setListCellChangedListener();
	}
	
	/**
	 * Used to define the Callback that will be called when a List item is selected
	 */
	private void setListCellChangedListener()
	{
		bookList.getSelectionModel().selectedItemProperty().addListener(
				
				new ChangeListener<String>(){

					@Override
					public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) 
					{
						
						//TODO add support for configuration files
						
						try {
							
							// log the event
							logger.info(String.format("book selected"));
							
							// Retrieve the view manager to show details about the book
							URL bookDetails = this.getClass().getResource("/View/Sample.fxml");
							
							view = ViewManager.getInstance();
							
							view.setCurrentPane(rootNode);
							
							view.switchView(bookDetails, new BookDetailController());
							
						} catch(Exception e)
						{
							logger.error(String.format("%s : %s", this.getClass().getName(), e.getMessage()));
						}
						
					}
				});
	}
	
	
	
	
	
	
}
