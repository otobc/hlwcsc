package extexp;

import java.util.Date;

import basic.HomoValue;

class RawData extends Data {
	
	public String nodeId;

	public RawData(String id, String name, String environmentId, String nodeId, Date time, HomoValue homoValue){
		super(id, name, environmentId, time, homoValue);
		this.nodeId = nodeId;
	}
	
	public String toString() {
		return "RawData(" + this.id + ", "
				+ this.name + ", "
				+ this.environmentId + ", "
				+ this.nodeId + ", "
				+ Extexp.dateToString(this.time) + ", "
				+ this.homoValue+ ")";
	}
}
