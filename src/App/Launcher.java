package App;
import Controller.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

/**
 * Launcher for Book inventory system
 * 
 * @author isaacbuitrago
 * CS 4743 Assignment 1 by Hercules Hjalmarsson & Isaac Buitrago
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
		
		stage.setTitle("Assignment 1");
		
		stage.setScene(scene);
		
		stage.show();

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
