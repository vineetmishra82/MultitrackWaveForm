package interfaces;

import java.io.Serializable;
import java.util.List;

public interface Track extends Serializable {
	
	public String getName();
	public List<Float> getList();

	public void setList(List<Float> list);

}
