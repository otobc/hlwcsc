package extexp;

import java.sql.ResultSet;
import java.util.Date;

import basic.HomoValue;

abstract class Data {
	public String id;
	public String name;
	public String environmentId;
	public Date time;
	public HomoValue homoValue;
	
	public Data(String id, String name, String environmentId, Date time, HomoValue homoValue) {
		this.id = id;
		this.name = name;
		this.environmentId = environmentId;
		this.time = time;
		this.homoValue = homoValue;
	}
	
	abstract public String toString();
	
	public static RawData genData(ResultSet rs) throws Exception {
		String id = rs.getString("id");
		String name = rs.getString("name");
		String environmentId = rs.getString("environmentid");
		String nodeId = rs.getString("nodeid");
		String dataType = rs.getString("datatype");
		String value = rs.getString("value");
		HomoValue homoValue = new HomoValue(dataType, value);
		Date time =  Extexp.todate(rs.getString("time"));
		return new RawData(id, name, environmentId, nodeId, time, homoValue);
	}
	
	public static RawData genData(String id, String name, String environmentId, String nodeId, Date time, HomoValue homoValue) {
		return new RawData(id, name, environmentId, nodeId, time, homoValue);
	}
	
	public static MergeData genData(String id, String name, String environmentId, Date time, HomoValue homoValue) {
		return new MergeData(id, name, environmentId, time, homoValue);
	}
}
