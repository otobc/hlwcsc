package extexp;

import java.util.Date;

class RawData extends Data {
	
	public String nodeId;

	public RawData(String id, String name, String environmentId, String nodeId, Date time, Object value){
		super(id, name, environmentId, time, value);
		this.nodeId = nodeId;
	}
	
	public String toString() {
		return "RawData(" + this.id + ", "
				+ this.name + ", "
				+ this.environmentId + ", "
				+ this.nodeId + ", "
				+ Extexp.dateToString(this.time) + ", "
				+ this.value+ ")";
	}
}
