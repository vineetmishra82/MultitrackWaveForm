package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import implementation.*;
import interfaces.Project;
import interfaces.SubGroup;
import interfaces.Track;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;

public class Controller implements Initializable {

	DirectoryChooser directoryChooser = new DirectoryChooser();
	FileChooser fileChooser = new FileChooser();
	List<Track> trackFiles = new ArrayList<>();
	Project loadedProject;
	
	@FXML
	private Button uploadBtn;

	@FXML
	private Text closeBtn;

	@FXML
	private ComboBox<Integer> tracks;

	@FXML
	private TextField trackStartNum;
	
	@FXML
	private Label statusMsgBelow;
	
	@FXML
	private Label statusMsg;
	
	@FXML
	private Label projectName; 
	
	@FXML
	private AnchorPane tracksPanel;
	
	
	@FXML
	public void createProject(ActionEvent e) {		
		
		if(trackFiles.isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Empty tracks list");
			alert.setContentText("No tracks to add. "
					+ "Upload some tracks and then try to "
					+ "create a project");
			alert.showAndWait();
		}
		else{
	
	        TextInputDialog td = new TextInputDialog("Project Name");
	  
	        // setHeaderText
	        td.setHeaderText("Create New Project");
	  
	       Optional<String> projectName = td.showAndWait();
	       
	      projectName.ifPresent(name -> {
	    	  
	    	 List<SubGroup> subGroups = new ArrayList<>();   
	    	 List<Track> trackGroup = new ArrayList<>();
	    	 
	    	 trackGroup.addAll(trackFiles);
	    	 
	    	 loadedProject = new ProjectImpl(name, subGroups, trackGroup);
	    	 
	    	 //Emptying current buffer in trackfiles
	    	 
	    	 trackFiles.clear();
	    	 
	    	 directoryChooser.setTitle("Select folder to save");
	    	 File selectedDirectory = directoryChooser.showDialog(null);
	    	 
	    	 
	    	 String fileName = selectedDirectory+"/"+loadedProject.getName()+".waveformfile";
	    	 
	    	 FileOutputStream file;
			try {
				file = new FileOutputStream(fileName);
				ObjectOutputStream out = new ObjectOutputStream(file);
				
				 out.writeObject(loadedProject);
	             
		            out.close();
		            file.close();
		            
		        	Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Success");
					alert.setContentText("Project saved at "+fileName);
					alert.showAndWait();
		            
			} catch (FileNotFoundException e1) {
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Could not save file as location is "
						+ "not available.");
				alert.showAndWait();
				
				
			} catch (IOException e1) {
				
				e1.printStackTrace();
				
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Error");
				alert.setContentText("Could not save file,reason -> "
						+ e1.getMessage());
				alert.showAndWait();
				
			}
	         
	    	  
	      });
		}
		
		
	}
	

	@FXML
	public void uploadFiles(ActionEvent e) {
		List<File> files = fileChooser.showOpenMultipleDialog((Stage) closeBtn.getScene().getWindow());
		
		statusMsg.textProperty().setValue(files.size()+" file/s uploaded.");
		
		for (File file : files) {
			List<Float> list = new ArrayList<>();
			
			Path path = Paths.get(file.getAbsolutePath());
			try {
			 
			  Files.lines(path)
			  .forEach(new Consumer<String>() {

				@Override
				public void accept(String value) {
					// TODO Auto-generated method stub
					list.add(Float.parseFloat(value));
				}
			});//print each line
			  
			  TrackImpl track = new TrackImpl(list,("Track"+file.getName().replaceAll("dataFile", ""))); 
			  trackFiles.add(track);
			 
			} catch (IOException ex) {
			  ex.printStackTrace();
			  System.out.println("Error in file -> "+file.getName());//handle exception here
			}
		}
		
		for (Track track : trackFiles) {
			
			System.out.println("Length of file is "+track.getList().size());
		}
	
	}
	
	@FXML
	public void uploadProject(ActionEvent e) {
		
	
		ExtensionFilter extensionFilter = new ExtensionFilter("Wave Form Project","*.waveformfile");
		
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showOpenDialog((Stage) closeBtn.getScene().getWindow());
		
		try {
			
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			
			ProjectImpl project = (ProjectImpl)in.readObject();
			
			in.close();
			
			if(!project.equals(null))
			{
				loadedProject = project;
				
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("Success");
				alert.setContentText("Project loaded successfully");
				alert.showAndWait();
				
				//Loading project
				
				loadProjectToProcess(loadedProject);
				
			}
			else {
				throw new Exception();
			}
				
			
		} catch (Exception e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed");
			alert.setContentText("Could not load project");
			alert.showAndWait();
		}
		
	}

	private void loadProjectToProcess(Project loadedProject) {
		
		projectName.setText(loadedProject.getName());
		
		VBox trackBox = new VBox();
		VBox subGroupBox = new VBox(); 
		
		
		for (Track track : loadedProject.getTracks()) {
			HBox hBox = new HBox();
			
			hBox.setPrefHeight(50);
			
			Button button = new Button(track.getName());			
			button.setPrefWidth(tracksPanel.getWidth()*.8);
			
			
			Button button1 = new Button("-");
			button1.setPrefWidth(tracksPanel.getWidth()*.2);
			
			
			
			hBox.getChildren().addAll(button1,button);
			button.setMinHeight(hBox.getHeight());
			button1.setMinHeight(hBox.getHeight());
			
			trackBox.getChildren().add(hBox);
			VBox.setMargin(hBox, new Insets(0, 0, 5, 0));
		}
		
		for (SubGroup subGroup : loadedProject.getSubGroups()) {
			HBox hBox = new HBox();
			
			hBox.setPrefHeight(50);
			
			Button button = new Button(subGroup.getName());			
			button.setPrefWidth(tracksPanel.getWidth()*.8);
			
			
			Button button1 = new Button("-");
			button1.setPrefWidth(tracksPanel.getWidth()*.2);
			
			
			
			hBox.getChildren().addAll(button1,button);
			button.setMinHeight(hBox.getHeight());
			button1.setMinHeight(hBox.getHeight());
			
			subGroupBox.getChildren().add(hBox);
			VBox.setMargin(hBox, new Insets(0, 0, 5, 0));
		}
		
		
		
		
		tracksPanel.getChildren().add(trackBox);
		
	}


	@FXML
	public void closeApp(Event e) {
		Stage s = (Stage) closeBtn.getScene().getWindow();
		s.close();
	}

	@FXML
	public void generateTracks(ActionEvent ex) {

		if (Integer.parseInt(trackStartNum.getText()) >= 1 && tracks.getValue()!=null){
			 directoryChooser.setTitle("Select folder to upload tracks");
			File selectedDirectory = directoryChooser.showDialog(null);

			
			// Running for no of log files
			int fileNum = Integer.parseInt(trackStartNum.getText());
			
			for(int i =1;i<=tracks.getValue();i++)
			{
				List<String> numList = getNumList();
				
				Path output = Paths.get(selectedDirectory+"/dataFile"+fileNum);
				fileNum++;
			    try {
			        Files.write(output, numList);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}
			
			statusMsgBelow.textProperty().setValue(tracks.getValue()+" file/s created at "+selectedDirectory);
			
		}

	}

	private List<String> getNumList() {
		
		List<String> list = new ArrayList<>();
		
		Random random = new Random();
		
		int limit = random.nextInt(30000, 80001);
		
		for(int i =0;i<limit;i++)
		{
			Float value = (float) (10*Math.sin(0.01* i)+ 30*Math.sin(Math.sin(0.0001* i)*0.01*i));
			list.add(String.valueOf(value));
		}
		
		return list;
	}

	@FXML
	public void setTrackValue(Event e) {
		System.out.println("Combobox clicked - " + tracks.getValue());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ObservableList<Integer> list = FXCollections.observableArrayList();

		int i = 1;

		while (i < 11) {
			list.add(i);
			i++;
		}

		tracks.getItems().setAll(list);

		// For text field

		trackStartNum.textProperty().addListener((obs, oldValue, newValue) -> {

			trackStartNum.setText(newValue.replaceAll("[^\\d,]", ""));
			System.out.println(trackStartNum.getText());
			System.out.println(tracks.getValue().toString());

		});

	}

}
