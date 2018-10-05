package App;
import Controller.MenuController;
import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * CS 4743 Assignment 2 by Isaac Buitrago and Hercules Hjalmarsson
 * 
 * Launcher for Book inventory system
 * 
 * @author isaacbuitrago
 *
 */
public class Launcher extends Application 
{

	/**
	 *  Load the menu view, set the controller for the menu view, 
	 *  build the scene graph, set the stage with the scene,
	 *  and show the stage
	 */
	@Override
	public void start(Stage stage) throws Exception 
	{
		URL menu = this.getClass().getResource("/View/menu.fxml");
		
		FXMLLoader loader = new FXMLLoader(menu);
		
		loader.setController(new MenuController());
		
		Parent root = loader.load();
		
		Scene scene = new Scene(root);
		
		stage.setTitle("Assignment 2");
		
		stage.setScene(scene);
		
		stage.show();

	}
	
	
	/**
	 * Used to close the connection to the database
	 */
	@Override
	public void stop() throws Exception 
	{
		// close the MySQL connection
		DBConnection.getInstance().getConnection().close();
	}



	/**
	 * Entry point for Book inventory system
	 * @param args
	 */
	public static void main(String[] args) 
	{	
		launch(args);
	}

}
