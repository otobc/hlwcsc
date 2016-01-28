package newEvaluatioin;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;
import org.wltea.expression.datameta.BaseDataMeta.DataType;

import basic.LvlSts;
import basic.Sts;
import bean_json.EvaluateBean;
import meth.Meth;
import treeToJson.*;

public class EvaluationTree {
	public  ArrayList<ArrayList<EdgeNode>> tree = new ArrayList<ArrayList<EdgeNode>>();
	public  Node root;	//���ڵ㣬Ч������

	
	/**������ڵ�ֵ
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException */
	public void countroot(Node node) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//		�ҵ��ڵ�node��ArrayList�е�λ��
		int i = 0;
		while(tree.get(i).get(0).node != node)
			i++;

		int j = 1;
		while(j < tree.get(i).size()){
			if(tree.get(i).get(j).node.flag == false){
				countroot(tree.get(i).get(j).node);
				tree.get(i).get(j).node.flag = true;
			}
			j++;
		}
		
//		���ӽڵ����õ��ýڵ��ֵд������
		List<Double> weights = new ArrayList<Double>();
		List<Double> values = new ArrayList<Double>();
		for(int k = 1; k < tree.get(i).size(); k++){
			weights.add(tree.get(i).get(k).weight);
			values.add(tree.get(i).get(k).node.value);			 
		}
		
		if(tree.get(i).get(0).node.vertextype.equals("0")){	//��ʾ�ýڵ��Ƕ����ڵ�
			switch(tree.get(i).get(0).node.weighting){
//			��ʾ������Ȩƽ��
			case "01":
				tree.get(i).get(0).node.value = IntegretedMeth.wArithmeticMean(weights, values);
				break;
//			��ʾ���μ�Ȩƽ��
			case "02":
				tree.get(i).get(0).node.value = IntegretedMeth.wGeometricMean(weights, values);
				break;
			default:
				JsonStr.result = "01";
				JsonStr.message = "��Ȩ�ۺϷ�������";
//				System.out.println("��Ȩ�ۺϷ�������");
			}
			
			tree.get(i).get(0).node.level = tree.get(i).get(0).node.value;
		}
		else if(tree.get(i).get(0).node.vertextype.equals("0")){	//��ʾ�ýڵ��Ƕ��Խڵ�
						
			String[] vertexlevel = tree.get(i).get(0).node.vertexlevel.split(",");
			Integer[] vlevel = new Integer[vertexlevel.length];
			tree.get(i).get(0).node.sumOfLevel = vertexlevel.length;
			for(int k = 0; k < vertexlevel.length; k++){
				vertexlevel[i] = vertexlevel[i].trim();
				vlevel[i] = new Integer(vertexlevel[i]);
			}
			
			String[] mbsp = tree.get(i).get(0).node.mbsp.split("#");
			for(int k = 0; k < vertexlevel.length; k++){
				mbsp[i] = mbsp[i].trim();
			}
			
			String conclude = tree.get(i).get(0).node.conclude;
			tree.get(i).get(0).node.level = 
					(double) IntegretedMeth.fuzzyComprehensiveEvaluation(weights, values, vlevel, mbsp, conclude).intValue();

			
//			���м��������ٻ�
			if(tree.get(i).get(0).node.nmlvl.equals("00")){
				String expression = tree.get(i).get(0).node.nmidxexp;
				List<Variable> var = new ArrayList<Variable>();
				var.add(new Variable("x", DataType.DATATYPE_DOUBLE, tree.get(i).get(0).node.level));
				tree.get(i).get(0).node.value = 
						new Double(ExpressionEvaluator.evaluate(expression, var).toString());
			}
			else{							
				ArrayList<Integer> vlvl = new ArrayList<Integer>();
				for(int k = 0; k < vlevel.length; k++){
					vlvl.add(vlevel[i]);
				}
				Sts sts = new LvlSts(vlvl);
				tree.get(i).get(0).node.value =
						Meth.exenm(tree.get(i).get(0).node.nmlvl, sts, tree.get(i).get(0).node.level);
			}
			
			
		}
		else{
			JsonStr.result = "02";
			JsonStr.message = "�ڵ����ʹ���";
//			System.out.println("�ڵ����ʹ���");
		}
		
		
	}
	

	
	/**�������ݿ⽨������������������ʼ��
	 * @param isReadCache 
	 * @throws ParseException */
	public void initial(ArrayList<ArrayList<EdgeNode>> tree, String evaluateId) throws SQLException, UnsupportedEncodingException, ParseException{
//		this.evaluationId = evaluateId;
		ReadDatabase rdb = new ReadDatabase();
//		rdb.connectDatabase();
		ArrayList<Node> nodes = rdb.readNode(evaluateId);
		ArrayList<Edge> edges = rdb.readEdge(evaluateId);
//		rdb.close();
		
//		���Ҷ�ڵ�value
		for(int i = 0; i < nodes.size(); i++){
			if(nodes.get(i).nclass.equals("2")){
				String wpindexid = nodes.get(i).wpindexid;
				String expid = rdb.readExInfo(evaluateId).id;
				if(EvaluateBean.isReadCache.equals("0"))
					rdb.computeWpindexcache(wpindexid, evaluateId);
				nodes.get(i).value = rdb.getValueOfIndex(evaluateId, wpindexid, expid);
				nodes.get(i).flag = true;
			}
		}
		
//		�ҵ����ڵ㣬��ȷ�ϸ��ڵ��Ƿ�Ψһ
		int numOfRoot = 0;
		for(int i = 0; i < nodes.size(); i++){
			if(nodes.get(i).nclass.equals("0")){
				numOfRoot++;
				root = nodes.get(i);
			}
		}
		if(numOfRoot != 1){
			JsonStr.result = "03";
			JsonStr.message = "Number of root error! -" + numOfRoot;
//			System.out.println("Number of root error! -" + numOfRoot);
			return;
		}
		
//		�����������ڵ��ArrayList
		for(int i = 0; i < nodes.size(); i++){
			ArrayList<EdgeNode> list = new ArrayList<EdgeNode>();
			EdgeNode enode = new EdgeNode();
			enode.node = nodes.get(i);
			list.add(enode);
			tree.add(list);
		}
		
		/*���ݴ����ݿ��ж�ȡ�ı���Ϣ����������tree*/
		for(int i = 0; i < edges.size(); i++){
			
//			Ѱ�ұ߶�Ӧ��parent��tree�е�λ��(�ĸ�list)
			int pflag = 0;
			while(pflag < tree.size()){
				if(edges.get(i).parent.equals(tree.get(pflag).get(0).node.id))
					break;
				pflag++;
			}
			
//			Ѱ�ұ߶�Ӧ��child
			int cflag = 0;
			while(cflag < nodes.size()){
				if(edges.get(i).child.equals(nodes.get(cflag).id))
					break;
				cflag++;
			}
			
			EdgeNode enode = new EdgeNode();
			nodes.get(cflag).indegree++;
			nodes.get(pflag).outdegree++;
			enode.node = nodes.get(cflag);
			enode.weight = edges.get(i).weight;
			
			tree.get(pflag).add(enode);
		}
		
	}
	public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
		
	}
}
