package basic;

public class HomoValue {
	private Object value;
	
	private long min(long x, long y) {
		return (x < y) ? x : y;
	}
	
	private double min(double x, double y) {
		return (x < y) ? x : y;
	}
	
	private long max(long x, long y) {
		return (x > y) ? x : y;
	}
	
	private double max(double x, double y) {
		return (x > y) ? x : y;
	}
	
	public HomoValue() {
		this.value = null;
	}
	
	public HomoValue(String dataType, String value) throws Exception {
		if (dataType.equals(Definition.Datatype.STRING)) {
			this.value = value;
		}
		else if (dataType.equals(Definition.Datatype.LONG)) {
			this.value = Long.valueOf(value);
		}
		else if (dataType.equals(Definition.Datatype.DOUBLE)) {
			this.value = Double.valueOf(value);
		}
		else if (dataType.equals(Definition.Datatype.BOOLEAN)) {
			this.value = Boolean.valueOf(value);
		}
		else {
			throw new Exception(String.format("HomoValue，错误的数据类型"));
		}
	}
	
	public void add(HomoValue hv) throws Exception {
		if (hv.value instanceof Long) {
			this.value = (this.value == null) ? (long)hv.value : (long)this.value + (long)hv.value;
		}
		else if (hv.value instanceof Double) {
			this.value = (this.value == null) ? (double)hv.value: (double)this.value + (double)hv.value;
		}
		else {
			throw new Exception(String.format("add:错误的数据类型"));
		}
	}
	
	
	
	public void min(HomoValue hv) throws Exception {
		if (hv.value instanceof Long) {
			this.value = (this.value == null) ? (long)hv.value : min((long)this.value, (long)hv.value);
		}
		else if (hv.value instanceof Double) {
			this.value = (this.value == null) ? (double)hv.value: min((double)this.value, (double)hv.value);
		}
		else {
			throw new Exception(String.format("min:错误的数据类型"));
		}
	}
	
	public void max(HomoValue hv) throws Exception {
		if (hv.value instanceof Long) {
			this.value = (this.value == null) ? (long)hv.value : max((long)this.value, (long)hv.value);
		}
		else if (hv.value instanceof Double) {
			this.value = (this.value == null) ? (double)hv.value: max((double)this.value, (double)hv.value);
		}
		else {
			throw new Exception(String.format("max:错误的数据类型"));
		}
	}
	
	public void all(HomoValue hv) throws Exception {
		if (hv.value instanceof Boolean) {
			this.value = (this.value == null) ? (boolean)hv.value : (boolean)this.value & (boolean)hv.value;
		}
		else {
			throw new Exception(String.format("all:错误的数据类型"));
		}
	}
	
	public void any(HomoValue hv) throws Exception {
		if (hv.value instanceof Boolean) {
			this.value = (this.value == null) ? (boolean)hv.value : (boolean)this.value | (boolean)hv.value;
		}
		else {
			throw new Exception(String.format("any:错误的数据类型"));
		}
	}
	
	public void none(HomoValue hv) throws Exception {
		if (hv.value instanceof Boolean) {
			this.value = (this.value == null) ? !(boolean)hv.value : (boolean)this.value & !(boolean)hv.value;
		}
		else {
			throw new Exception(String.format("none:错误的数据类型"));
		}
	}
	
	public void avg(long count) throws Exception {
		if (this.value instanceof Long) {
			this.value = (long)this.value * 1.0 / count;
		}
		else if (this.value instanceof Double) {
			this.value = (double)this.value / count;
		}
		else {
			throw new Exception(String.format("avg:错误的数据类型"));
		}
	}
	
	public Object getValue() {
		return this.value;
	}
	
	public String toString() {
		return this.value == null ? "none" : this.value.toString();
	}
	
	public static void main(String[] args) throws Exception {
		HomoValue hv1 = new HomoValue();
		HomoValue hv2 = new HomoValue(Definition.Datatype.LONG, "1");
		hv1.add(hv2);
		System.out.println(hv1);
	}
}
