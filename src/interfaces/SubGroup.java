package interfaces;


import java.io.Serializable;
import java.util.List;

import javafx.scene.layout.VBox;

public interface SubGroup extends Serializable{
	
	public String getName();
	
	public List<Track> getTracks();
	
	public void addTrack(Track track);
	
	public void removeTrack(Track track);	
	
	public VBox getSubGroupVBox();

}
