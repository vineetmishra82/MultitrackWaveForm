package implementation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import interfaces.Track;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class TrackImpl implements Track {
	
	private static final long serialVersionUID = 1L;
	private List<Float> list;
	private String name;
	private LineChart<Number, Number> lineChart;
	
	
	public TrackImpl(List<Float> list, String name) {
		this.list = list;
		this.name = name;
		
	}

	public List<Float> getList() {
		return list;
	}

	public void setList(List<Float> list) {
		this.list = list;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}
	
	@Override
	public HBox getChart() {
		HBox hBox = new HBox();
		HBox bBox = new HBox();
		VBox bBoxSet = new VBox(); 
		HBox colorPaletteBox = new HBox();
			
		Button button = new Button(this.getName());					
		Button button1 = new Button("-");	
		bBox.getChildren().addAll(button1,button);
		bBox.setSpacing(3);
		
		
		VBox.setMargin(colorPaletteBox, new Insets(0, 0, 0, 20));
	//	Adding color palette
		
		ColorPicker colorPicker = new ColorPicker();
		colorPaletteBox.getChildren().add(colorPicker);		
		colorPaletteBox.setSpacing(10);	
			
				
		
		bBoxSet.getChildren().addAll(bBox,colorPaletteBox);
		bBoxSet.setSpacing(20);
	
		//Setting Graph for this track
		
		Series<Number, Number> series = new Series<>();
		
		
		int i = 1;
		for(Float value : this.getList())
		{
			series.getData().add(new XYChart.Data<Number, Number>(i,value));
			i++;
		}
		
		lineChart = new LineChart<>(new NumberAxis(),new NumberAxis());
		lineChart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
		lineChart.getData().add(series);
		
		lineChart.getYAxis().setTickLabelsVisible(false);
		lineChart.getYAxis().setOpacity(0);
		lineChart.setPrefHeight(150); //Line chart height
		
		lineChart.getXAxis().setTickLabelsVisible(false);
		lineChart.getXAxis().setOpacity(0);
		
		
		lineChart.setCreateSymbols(false);
		
		lineChart.applyCss();
		lineChart.setPrefWidth(this.getList().size());
		
	
		
		HBox wBox = new HBox();
		wBox.setPrefHeight(120);
		wBox.getChildren().add(lineChart);
		
		ScrollPane scrollBar = new ScrollPane();
		scrollBar.setContent(wBox);
		
		
		hBox.getChildren().addAll(bBoxSet,scrollBar);
		hBox.setSpacing(5);
		
		button.setPrefWidth(100);
		button.setMinWidth(100);
		button.setMaxWidth(100);
		
		
		button1.setPrefWidth(30);
		button1.setMinWidth(30);
		button1.setMaxWidth(30);
		
		HBox.setMargin(bBoxSet, new Insets(50, 0, 0,0 ));
		
		
		AnchorPane.setLeftAnchor(scrollBar, 0.0);
		AnchorPane.setTopAnchor(scrollBar, 0.0);
		AnchorPane.setBottomAnchor(scrollBar, 0.0);
		AnchorPane.setRightAnchor(scrollBar, 0.0);
				
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				Color color = colorPicker.getValue();
				System.out.println("Chosen color - "+color.toString());
				String rgb = color.toString().replace("0x", "#");
				
				series.getNode().setStyle("-fx-stroke: "+rgb+"; ");
								
			}
		});		
				
		return hBox;
		
	}

	public LineChart<Number, Number> getLineChart() {
		return lineChart;
	}

	
	@Override
	public String toString() {
		return "TrackImpl [list count=" + list.size() + ", name=" + name + "]";
	}

}
