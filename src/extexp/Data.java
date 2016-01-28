package extexp;

import java.sql.ResultSet;
import java.util.Date;

import basic.Definition;

abstract class Data {
	public String id;
	public String name;
	public String environmentId;
	public Date time;
	public Object value;
	
	public Data(String id, String name, String environmentId, Date time, Object value) {
		this.id = id;
		this.name = name;
		this.environmentId = environmentId;
		this.time = time;
		this.value = value;
	}
	
	abstract public String toString();
	
	public static RawData genData(ResultSet rs) throws Exception {
		String id = rs.getString("id");
		String name = rs.getString("name");
		String environmentId = rs.getString("environmentid");
		String nodeId = rs.getString("nodeid");
		String dataType = rs.getString("datatype");
		Object value = null;
		String sValue = rs.getString("value");
		if (dataType.equals(Definition.Datatype.STRING)) {
			value = sValue;
		}
		else if (dataType.equals(Definition.Datatype.LONG)) {
			value = Long.valueOf(sValue);
		}
		else if (dataType.equals(Definition.Datatype.DOUBLE)) {
			value = Double.valueOf(sValue);
		}
		else if (dataType.equals(Definition.Datatype.BOOLEAN)) {
			value = Boolean.valueOf(sValue);
		}
		else {
			throw new Exception(String.format("genData，错误的数据类型"));
		}
		Date time =  Extexp.todate(rs.getString("time"));
		return new RawData(id, name, environmentId, nodeId, time, value);
	}
	
	public static RawData genData(String id, String name, String environmentId, String nodeId, Date time, Object value) {
		return new RawData(id, name, environmentId, nodeId, time, value);
	}
	
	public static MergeData genData(String id, String name, String environmentId, Date time,
			Object value) {
		return new MergeData(id, name, environmentId, time, value);
	}
}
