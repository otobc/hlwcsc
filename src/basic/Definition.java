package basic;

// ö���ͼ���ö���Ͷ����ڴ�
public class Definition {
	
	public static final double threhold = 0.000000001;
	
	// �����ںϺ����Ķ������
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
	
	// ��������
	public class Datatype {
		public static final String STRING = "0";
		public static final String LONG = "1";
		public static final String DOUBLE = "2";
		public static final String BOOLEAN = "3";
	}
	
	public static void main(String[] args){

	}
}
