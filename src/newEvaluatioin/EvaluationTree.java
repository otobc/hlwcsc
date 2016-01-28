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
	public  Node root;	//根节点，效果向量

	
	/**计算根节点值
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException */
	public void countroot(Node node) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
//		找到节点node在ArrayList中的位置
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
		
//		由子节点计算得到该节点的值写在这里
		List<Double> weights = new ArrayList<Double>();
		List<Double> values = new ArrayList<Double>();
		for(int k = 1; k < tree.get(i).size(); k++){
			weights.add(tree.get(i).get(k).weight);
			values.add(tree.get(i).get(k).node.value);			 
		}
		
		if(tree.get(i).get(0).node.vertextype.equals("0")){	//表示该节点是定量节点
			switch(tree.get(i).get(0).node.weighting){
//			表示算数加权平均
			case "01":
				tree.get(i).get(0).node.value = IntegretedMeth.wArithmeticMean(weights, values);
				break;
//			表示几何加权平均
			case "02":
				tree.get(i).get(0).node.value = IntegretedMeth.wGeometricMean(weights, values);
				break;
			default:
				JsonStr.result = "01";
				JsonStr.message = "加权聚合方法错误！";
//				System.out.println("加权聚合方法错误！");
			}
			
			tree.get(i).get(0).node.level = tree.get(i).get(0).node.value;
		}
		else if(tree.get(i).get(0).node.vertextype.equals("0")){	//表示该节点是定性节点
						
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

			
//			进行级别无量纲化
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
			JsonStr.message = "节点类型错误！";
//			System.out.println("节点类型错误！");
		}
		
		
	}
	

	
	/**按照数据库建立评估树，并对树初始化
	 * @param isReadCache 
	 * @throws ParseException */
	public void initial(ArrayList<ArrayList<EdgeNode>> tree, String evaluateId) throws SQLException, UnsupportedEncodingException, ParseException{
//		this.evaluationId = evaluateId;
		ReadDatabase rdb = new ReadDatabase();
//		rdb.connectDatabase();
		ArrayList<Node> nodes = rdb.readNode(evaluateId);
		ArrayList<Edge> edges = rdb.readEdge(evaluateId);
//		rdb.close();
		
//		获得叶节点value
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
		
//		找到根节点，并确认根节点是否唯一
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
		
//		逐条建立各节点的ArrayList
		for(int i = 0; i < nodes.size(); i++){
			ArrayList<EdgeNode> list = new ArrayList<EdgeNode>();
			EdgeNode enode = new EdgeNode();
			enode.node = nodes.get(i);
			list.add(enode);
			tree.add(list);
		}
		
		/*根据从数据库中读取的边信息，逐条建立tree*/
		for(int i = 0; i < edges.size(); i++){
			
//			寻找边对应的parent在tree中的位置(哪个list)
			int pflag = 0;
			while(pflag < tree.size()){
				if(edges.get(i).parent.equals(tree.get(pflag).get(0).node.id))
					break;
				pflag++;
			}
			
//			寻找边对应的child
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
