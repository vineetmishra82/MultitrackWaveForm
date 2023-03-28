package application;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import org.controlsfx.control.RangeSlider;

import implementation.*;
import interfaces.Project;
import interfaces.SubGroup;
import interfaces.Track;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Controller implements Initializable {

	DirectoryChooser directoryChooser = new DirectoryChooser();
	FileChooser fileChooser = new FileChooser();
	List<Track> trackFiles = new ArrayList<>();
	Project loadedProject;
	
	@FXML
	private Button uploadBtn;

	@FXML
	private Button projectBtn;
	
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
	private ScrollPane scrollPane;
	
	@FXML
	private HBox zoomBox;
	
	@FXML
	private HBox sliderBox;
	
	@FXML
	private HBox projectBar;
	
	@FXML
	private ComboBox<String> addData;
	
	private Slider zoomBar = new Slider();
	
	private RangeSlider rslider = new RangeSlider();
	
	private Rectangle rect = new Rectangle();
	
	private VBox mainVBox= new VBox();
	
	private VBox projectVBox = new VBox();
	
	private int largestTrackCount = 0;
	
	
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
		
		FileChooser fileChooser = new FileChooser(); 
		
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
				loadedProject.setProjectPath(file.getPath());
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Project Loading");
				alert.setContentText("Press ok to start loading");
				alert.showAndWait();
				
				loadProjectToProcess(loadedProject);
				//Loading project
				
				
				projectBar.setVisible(true);
									
								
			}
			else {
				throw new Exception();
			}
				
			
		} catch (Exception e2) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed");
			alert.setContentText("Could not load project");
			alert.showAndWait();
			System.out.println(e2.getLocalizedMessage());
			e2.printStackTrace();
		}
		
	}

	private void loadProjectToProcess(Project loadedProject) {
		
		largestTrackCount = 0;
		mainVBox.getChildren().clear();
		//Setting LHS Tracks panel
		projectName.setText(loadedProject.getName());
			
		
	
		for (Track track : loadedProject.getTracks()) {
			
			addTracktoProject(track,projectVBox);		
		
		}
		
		for (SubGroup subGroup : loadedProject.getSubGroups()) {
			
		}			
	
	}

	private void addTracktoProject(Track track,VBox vBox) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				
				HBox bBox = new HBox();
				VBox bBoxSet = new VBox(); 
				HBox colorPaletteBox = new HBox();
					
				Button button = new Button(track.getName());					
				Button removeTrackButton = new Button("-");	
				bBox.getChildren().addAll(removeTrackButton,button);
				bBox.setSpacing(3);	
				
				
				VBox.setMargin(colorPaletteBox, new Insets(0, 0, 0, 20));
			//	Adding color palette
				
				ColorPicker colorPicker = new ColorPicker();
				colorPaletteBox.getChildren().add(colorPicker);		
				colorPaletteBox.setSpacing(10);							
				
				bBoxSet.getChildren().addAll(bBox,colorPaletteBox);
				bBoxSet.setSpacing(20);
				
				button.setPrefWidth(100);
				button.setMinWidth(100);
				button.setMaxWidth(100);
				
				
				removeTrackButton.setPrefWidth(30);
				removeTrackButton.setMinWidth(30);
				removeTrackButton.setMaxWidth(30);
				
				HBox.setMargin(bBoxSet, new Insets(50, 0, 0,0 ));
										
				colorPicker.setOnAction(new EventHandler<ActionEvent>() {
					
					@Override
					public void handle(ActionEvent arg0) {
						
						Color color = colorPicker.getValue();
						System.out.println("Chosen color - "+color.toString());
						String rgb = color.toString().replace("0x", "#");
						
						track.getLineChart().getData()
						.get(0).getNode().setStyle("-fx-stroke: "+rgb+"; ");
										
					}
				});		
				
				System.out.println(track.getName()+" started");
				HBox box = track.getChart();
				box.getChildren().add(0, bBoxSet);
				vBox.getChildren().addAll(box);
				System.out.println(track.getName()+" is done");
				
				removeTrackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {	
						
						ButtonType confirmButton = new ButtonType("Yes");
						ButtonType cancelButton = new ButtonType("No");
						Alert confAlert = new Alert(AlertType.WARNING);
						confAlert.setHeaderText("Confirm delete");
						confAlert.setContentText("Cannot be undone. Are you sure you want to delete "
						+track.getName()+" ?");
						confAlert.getButtonTypes().clear();
						confAlert.getButtonTypes().addAll(confirmButton,cancelButton);
						
						confAlert.showAndWait().ifPresent(response -> {
							if(response == confirmButton)
							{
								vBox.getChildren().remove(box);
								loadedProject.getTracks().remove(track);
								
								if(loadedProject.getTracks().size()<=0)
								{
									sliderBox.setVisible(false);
								}
								
								System.out.println(loadedProject.getProjectPath());
								saveProject();
							}
						});
						
						
					}

					
				});
			}
			
		});						
				
		
		if(track.getList().size() > largestTrackCount)
		{
			largestTrackCount = track.getList().size();
		}
		
		
		
		rslider = new RangeSlider(0, largestTrackCount, 0, 100);
		
		rslider.setShowTickLabels(true);
		rslider.setShowTickMarks(true);
		rslider.setMinorTickCount(largestTrackCount);
		
		
		 rslider.skinProperty().addListener(new ChangeListener<>() {
	            @Override public void changed(ObservableValue<? extends Skin<?>> observable, Skin<?> oldValue, Skin<?> newValue) {
	                if (newValue != null) {
	                    observable.removeListener(this);

	                    Node lowThumb = rslider.lookup(".low-thumb");
	                    Node highThumb = rslider.lookup(".high-thumb");
	                    
	                  
	                    rect.setFill(Color.rgb(251,255, 0,0.16));
	                    rect.setManaged(false);
	                    rect.setMouseTransparent(true);
	                    
	                    

	                    rect.yProperty().bind(rslider.layoutYProperty().add(rslider.heightProperty()));
	                    rect.heightProperty().bind(sliderBox.heightProperty().subtract(rect.yProperty()));

	                    rect.xProperty().bind(Bindings.createDoubleBinding(() -> {
	                        Bounds bounds = lowThumb.getParent().localToScene(lowThumb.getBoundsInParent());
	                        bounds = sliderBox.sceneToLocal(bounds);
	                        return bounds.getCenterX();
	                    }, lowThumb.boundsInParentProperty()));
	                    rect.widthProperty().bind(Bindings.createDoubleBinding(() -> {
	                        Bounds lowBounds = lowThumb.getParent().localToScene(lowThumb.getBoundsInParent());
	                        lowBounds = sliderBox.sceneToLocal(lowBounds);
	                        Bounds highBounds = highThumb.getParent().localToScene(highThumb.getBoundsInParent());
	                        highBounds = sliderBox.sceneToLocal(highBounds);
	                        return highBounds.getCenterX() - lowBounds.getCenterX();
	                    }, lowThumb.boundsInParentProperty(), highThumb.boundsInParentProperty()));

	                    sliderBox.getChildren().add(rect);
	                }
	            };
	        });
	        
				 
		sliderBox.getChildren().clear();
		sliderBox.getChildren().addAll(rslider);
		sliderBox.toBack();	
		
		if(loadedProject.getTracks().size()>0)
		{
			sliderBox.setVisible(true);
			zoomBox.setVisible(true);
		}
			
		 StackPane content = new StackPane(vBox, sliderBox);
		 StackPane.setAlignment(content.getChildren().get(0), Pos.CENTER_LEFT);
	     StackPane.setMargin(content.getChildren().get(0), new Insets(50,0,0,0));
	     StackPane.setMargin(content.getChildren().get(1), new Insets(0,0,0,175));
		
		scrollPane.setContent(content);		
		rslider.setPrefWidth(largestTrackCount);
	}
	
	private void saveProject() {
		try {
			FileOutputStream file = new FileOutputStream(loadedProject.getProjectPath());
			ObjectOutputStream out = new ObjectOutputStream(file);
			
			 out.writeObject(loadedProject);
             
	            out.close();
	            file.close();
	           
	            
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
		
			
		int limit = (int) Math.floor(Math.random() *(30000 - 80001 + 1) + 30000);
		//random.nextInt(30000, 80001);
		
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
		
		mainVBox.getChildren().add(projectVBox);
		
		//Adding combox items
		
		addData.getItems().add("+");
		addData.getItems().add("Add Track");
		addData.getItems().add("Add Sub Group");
		
		addData.setOnAction(event->{
			
				if(addData.getSelectionModel().getSelectedIndex()>=1)
				{
					if(addData.getSelectionModel().getSelectedIndex()==1)
					{
						addingTrackToExistingMainProject();
					}
					
					else {
						//Adding Subgroup
						
						  TextInputDialog td = new TextInputDialog("Create Subgroup");
						  
					        // setHeaderText
					        td.setHeaderText("Enter Subgroup name");
					  
					       Optional<String> projectName = td.showAndWait();
					       
					      projectName.ifPresent(name -> {
					    	  
					    	  SubGroup subGroup = new SubGroupImpl(name);
					    	  
					    	  generateSubGroupItems(subGroup);	
					    	  
					    	  loadedProject.getSubGroups().add(subGroup);
					    	  
					    	  mainVBox.getChildren().add(subGroup.getSubGroupVBox());
					    	  System.out.println("Subgroups - "+loadedProject.getSubGroups());
					    	  saveProject();
					      });
					}
				}			
				
				addData.setValue("+");
				
		});

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
		
		//Setting Scroll pane settings
		
		AnchorPane.setBottomAnchor(scrollPane, 0.0);
		AnchorPane.setTopAnchor(scrollPane, 0.0);
		AnchorPane.setLeftAnchor(scrollPane, 0.0);
		AnchorPane.setRightAnchor(scrollPane, 0.0);
		
		InputStream i1 = null;
		InputStream i2 = null;
		try {
			i1 = new FileInputStream("./assets/zoomOut.jpeg");
			i2 = new FileInputStream("./assets/zoomIn.png");
			
			File file1 = new File("./assets/zoomOut.jpeg"); 
			File file2 = new File("./assets/zoomIn.png"); 
			
			System.out.println("File1 - "+file1.exists()+" File2 - "+file2.exists());
			
			Image zoomOutImage = new Image(i1);
			Image zoomInImage = new Image(i2);
			
			ImageView zoomIn = new ImageView();
			zoomIn.setImage(zoomInImage);
			zoomIn.setFitWidth(20);
			zoomIn.setFitHeight(20);
			
			
			ImageView zoomOut = new ImageView();
			zoomOut.setImage(zoomOutImage);
			zoomOut.setFitWidth(20);
			zoomOut.setFitHeight(20);
			
			
			zoomBar.setPrefHeight(16);
			zoomBar.setPrefWidth(190);
			zoomBar.setMin(0);
			zoomBar.setMax(100);
			zoomBar.setValue(0);
			zoomBar.setBlockIncrement(10);
			
			zoomBar.valueProperty().addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
					
					double val = newVal.doubleValue() - oldVal.doubleValue();
					
					for (Track track : loadedProject.getTracks()) {
					System.out.println("Track width - "+track.getLineChart().getWidth());
						double width =track.getLineChart().getWidth() * (1.0-(val/10));
					
					track.getLineChart().setPrefWidth(width);	
					}
					double width =rslider.getWidth() * (1.0-(val/10));
					
					rslider.setPrefWidth(width);	
				}
				
			});			
			
			zoomBox.getChildren().addAll(zoomIn,zoomBar,zoomOut);
			zoomBox.setSpacing(5);
			
						
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		zoomBox.setVisible(false);
		projectBar.setVisible(false);
		sliderBox.setVisible(false);

		 ChangeListener<Scene> initializer = new ChangeListener<Scene>() {
	            @Override
	            public void changed(ObservableValue<? extends Scene> obs, Scene oldScene, Scene newScene)  {
	                if (newScene != null) {
	                    rslider.applyCss();
	                    rslider.getParent().layout();
	                    Pane thumb = (Pane) rslider.lookup(".range-slider .low-thumb");
	                    System.out.println(thumb); // <-- No longer null
	                    rslider.sceneProperty().removeListener(this);
	                }
	            }
	        };
	        rslider.sceneProperty().addListener(initializer);
	}


	private void generateSubGroupItems(SubGroup subGroup) {
		
	Button button = new Button(subGroup.getName());					
  	Button removeSubgroupButton = new Button("-");
  	Button addTrackToSubgroupButton = new Button("+");
  	
  	HBox hBox = new HBox();
  	hBox.getChildren().addAll(removeSubgroupButton,button,addTrackToSubgroupButton);
  	subGroup.getSubGroupVBox().getChildren().add(hBox);
  	  	
		
	}


	private void addingTrackToExistingMainProject() {
		
		FileChooser fileChooser = new FileChooser(); 
		
		List<File> files = fileChooser.showOpenMultipleDialog((Stage) closeBtn.getScene().getWindow());
		
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
			  
			  loadedProject.getTracks().add(track);
			 					  
			 
			} catch (IOException ex) {
			  ex.printStackTrace();
			  System.out.println("Error in file -> "+file.getName());//handle exception here
			}
		}
		
		 for (Track track1 : loadedProject.getTracks()) {
				
			  addTracktoProject(track1,projectVBox);
		}
		
	}

}
