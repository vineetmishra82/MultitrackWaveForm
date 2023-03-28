package implementation;

import java.util.List;

import interfaces.Project;
import interfaces.SubGroup;
import interfaces.Track;

public class ProjectImpl implements Project {

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String projectPath;
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	private List<SubGroup> subGroups;
	
	private List<Track> tracks;
	
	
	public ProjectImpl(String name, List<SubGroup> subGroups, List<Track> tracks) {
		this.name = name;
		this.subGroups = subGroups;
		this.tracks = tracks;
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
	public List<SubGroup> getSubGroups() {
		// TODO Auto-generated method stub
		return this.subGroups;
	}

	@Override
	public void addSubGroup(SubGroup subGroup) {
		this.subGroups.add(subGroup);

	}

	@Override
	public void removeSubGroup(SubGroup subGroup) {
		this.subGroups.remove(subGroup);

	}

	@Override
	public void addTrack(Track track) {
		this.tracks.add(track);

	}

	@Override
	public void removeTrack(Track track) {
		this.tracks.remove(track);

	}


}
