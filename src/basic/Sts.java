package basic;

public abstract class Sts {	
	public long lmax;
	public double dmax;
	public long lmin;
	public double dmin;
	public long lsum;
	double dsum;
	public long count;
	public double lavg;
	public double davg;
	public double lvar;
	public double dvar;
	public double tp; // ��ֵΪ�����ͻ����ַ����ͣ���tpΪ�������������еı���
	
	public abstract void setSts() throws Exception;
	
	public String toString() {
		return "lmax:" + lmax + "|dmax:" + dmax + "|lmin:" + lmin + "|dmin:" + dmin +
				"|lsum:" + lsum + "|dsum:" + dsum + "|count:" + count +
				"|lavg:" + lavg + "|davg:" + davg + "|lvar:" + lvar+ "|dvar:" + dvar +
				"|tp:" + tp;
	}
}