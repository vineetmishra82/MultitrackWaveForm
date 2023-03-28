package implementation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import interfaces.Track;
import javafx.application.Platform;
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
	private transient LineChart<Number, Number> lineChart;
	
	
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
		
	
		//Setting Graph for this track
		
		Series<Number, Number> series = new Series<>();
		
			
		for(Float value : this.getList())
		{
			Platform.runLater(new Runnable() {
				
				@Override
				public void run() {
					series.getData().add(new XYChart.Data<Number, Number>(series.getData().size(),value));
					
				}
			});
				
			
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
		
	
		
//		HBox wBox = new HBox();
//		wBox.setPrefHeight(120);
//		wBox.getChildren().add(lineChart);
		
		ScrollPane scrollBar = new ScrollPane();
		scrollBar.setContent(lineChart);
		
		
		
		hBox.getChildren().addAll(scrollBar);
		hBox.setSpacing(5);
		
						
		return hBox;
		
	}

	public LineChart<Number, Number> getLineChart() {
		return lineChart;
	}

	
	@Override
	public String toString() {
		return "TrackImpl [list count=" + list.size() + ", name=" + name + "]";
	}

	@Override
	public void nullifyLineChart() {
		// TODO Auto-generated method stub
		this.lineChart = null;
	}

}
