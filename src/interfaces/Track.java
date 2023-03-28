package interfaces;

import java.io.Serializable;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.layout.HBox;

public interface Track extends Serializable {
	
	public String getName();
	public List<Float> getList();

	public void setList(List<Float> list);
	
	public HBox getChart();
	
	public LineChart<Number, Number> getLineChart();
	
	public void nullifyLineChart();
	
}
