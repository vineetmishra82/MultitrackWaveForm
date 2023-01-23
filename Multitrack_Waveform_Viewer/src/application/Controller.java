package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;


public class Controller  implements Initializable {
	
	@FXML
	private Button uploadBtn;
	
	@FXML
	private Text closeBtn;
	
	@FXML
	private ComboBox<Integer> tracks;
	
	@FXML
	public void onBtnClick(ActionEvent e) {
		System.out.println("I am clicked");
	}
	
	@FXML
	public void closeApp(Event e)
	{
		Stage s = (Stage) closeBtn.getScene().getWindow();
        s.close();
	}
	
	
	
	@FXML
	public void setTrackValue(Event e)
	{
		System.out.println("Combobox clicked");
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ObservableList<Integer> list = FXCollections.observableArrayList();
		
		int i = 1;
		
		while(i<11)
		{
			list.add(i);
			i++;
		}
		
		System.out.println(list);
		
		tracks.getItems().setAll(list);
		
	}
	
}
