package extexp;

import java.util.Date;

class MergeData extends Data {

	public MergeData(String id, String name, String environmentId, Date time, Object value) {
		super(id, name, environmentId, time, value);
	}
	
	public String toString() {
		return "MergeData(" 
				+ this.id + ", "
				+ this.name + ", "
				+ this.environmentId + ", "
				+ Extexp.dateToString(this.time) + ", "
				+ this.value +")";
	}
}
