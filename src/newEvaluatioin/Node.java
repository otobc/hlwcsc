package newEvaluatioin;
//���ṹ
public class Node {
	public Double value = new Double(0.0d);	//���ֵ
	public Double level = new Double(0);	//���Խڵ��ǽڵ㼶��
	public int indegree = 0;	//������
	public int outdegree = 0;	//������
	public boolean flag = false; //��¼�Ƿ��Ѽ�������
	public Integer sumOfLevel = new Integer(0);	//�ܹ�������
	
	/*���±��������ݿ��Ӧ*/
	public String evaluateid = new String();	
	public String id = new String();	//�����
	public String name = new String();
	public String nclass = new String();
	public String vertextype = new String();
	public String wpindexid = new String();
	public String nmidix = new String();
	public String nmidxexp = new String();
	public String vertexlevel = new String();
	public String nmlvl = new String();
	public String nmlvlexp = new String();
	public String mbsp = new String();
	public String conclude = new String();
	public String weighting = new String();
}
