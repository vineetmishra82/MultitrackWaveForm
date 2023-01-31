package implementation;

import java.util.List;

import interfaces.Track;

public class TrackImpl implements Track {
	
	private static final long serialVersionUID = 1L;
	private List<Float> list;
	private String name;
	
	
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
	public String toString() {
		return "TrackImpl [list count=" + list.size() + ", name=" + name + "]";
	}
	


}
