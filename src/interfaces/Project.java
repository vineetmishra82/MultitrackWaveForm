package interfaces;

import java.io.Serializable;
import java.util.List;

public interface Project extends Serializable {
	
	public String getName();
		
	public List<Track> getTracks();
	
	public String getProjectPath();
	
	public void setProjectPath(String path);
	
	public List<SubGroup> getSubGroups();
	
	public void addSubGroup(SubGroup subGroup);
	
	public void removeSubGroup(SubGroup subGroup);
	
	public void addTrack(Track track);
	
	public void removeTrack(Track track);
	

}
