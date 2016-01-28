package newEvaluatioin;
//结点结构
public class Node {
	public Double value = new Double(0.0d);	//结点值
	public Double level = new Double(0);	//定性节点是节点级别
	public int indegree = 0;	//结点入度
	public int outdegree = 0;	//结点出度
	public boolean flag = false; //记录是否已计算出结果
	public Integer sumOfLevel = new Integer(0);	//总共级别数
	
	/*以下变量与数据库对应*/
	public String evaluateid = new String();	
	public String id = new String();	//结点编号
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
