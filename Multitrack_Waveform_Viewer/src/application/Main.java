package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		  System.out.println("Running");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println("Running");
			
				
			Parent root = FXMLLoader.load(getClass().getResource("UI_WaveForm.fxml"));

	        primaryStage.setScene(new Scene(root,650,500));
	        primaryStage.show();
       
	      
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	

}
