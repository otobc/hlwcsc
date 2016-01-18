package basic;

// 枚举型及类枚举型定义在此
public class Definition {
	
	public static final double threhold = 0.000000001;
	
	// 基础融合函数的额外参数
	public static class MergeBasicArgs {
		public static final int AVG = 0;
		public static final int SUM = 1;
		public static final int MAX = 2;
		public static final int MIN = 3;
		public static final int ALL = 4;
		public static final int ANY = 5;
		public static final int NONE = 6;
	}
	
	public static class EstimateBasicArgs {
		public static final int AVG = 0;
		public static final int MAX = 1;
		public static final int MIN = 2;
		public static final int NEAREST = 3;
		public static final int EARLIER = 4;
		public static final int LATER = 5;
	}
	
	// 数据类型
	public class Datatype {
		public static final String STRING = "0";
		public static final String LONG = "1";
		public static final String DOUBLE = "2";
		public static final String BOOLEAN = "3";
	}
	
	public static void main(String[] args){

	}
}
