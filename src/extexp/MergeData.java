package extexp;

import java.util.Date;

import basic.HomoValue;

class MergeData extends Data {

	public MergeData(String id, String name, String environmentId, Date time, HomoValue homoValue) {
		super(id, name, environmentId, time, homoValue);
	}
	
	public String toString() {
		return "MergeData(" 
				+ this.id + ", "
				+ this.name + ", "
				+ this.environmentId + ", "
				+ Extexp.dateToString(this.time) + ", "
				+ this.homoValue +")";
	}
}
