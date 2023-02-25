package implementation;

import java.util.List;

import interfaces.SubGroup;
import interfaces.Track;

public class SubGroupImpl implements SubGroup {
	
	private static final long serialVersionUID = 1L;
	private List<Track> tracks;
	private String name;
	
	public SubGroupImpl(List<Track> tracks, String name) {
		this.tracks = tracks;
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

}
