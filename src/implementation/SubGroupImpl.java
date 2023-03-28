package implementation;

import java.util.ArrayList;
import java.util.List;

import interfaces.SubGroup;
import interfaces.Track;
import javafx.scene.layout.VBox;

public class SubGroupImpl implements SubGroup {
	
	private static final long serialVersionUID = 1L;
	private List<Track> tracks;
	private String name;
	private transient VBox subgroupVBox = new VBox();;
	
	
	public SubGroupImpl(String name) {
		this.tracks = new ArrayList<>();
		this.name = name;
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public List<Track> getTracks() {
		// TODO Auto-generated method stub
		return this.tracks;
	}

	@Override
	public void addTrack(Track track) {
		
		tracks.add(track);

	}

	@Override
	public void removeTrack(Track track) {
		
		tracks.remove(track);
	}

	@Override
	public VBox getSubGroupVBox() {
		// TODO Auto-generated method stub
		return this.subgroupVBox;
	}

}
