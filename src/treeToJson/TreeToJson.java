package treeToJson;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import newEvaluatioin.*;

public class TreeToJson {
	public String toJson(String evaluateId) throws SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JsonGenerationException, JsonMappingException, IOException, ParseException{
		String result = new String();		
		EvaluationTree etree = new EvaluationTree();
		etree.initial(etree.tree, evaluateId);
		
		/*begin
		 * 给叶节点赋值，暂时用于测试*/
//		etree.tree.get(1).get(0).node.value = new Double(0.2d);
//		etree.tree.get(2).get(0).node.value = new Double(0.4d);
//		etree.tree.get(5).get(0).node.value = new Double(6.0d);
//		etree.tree.get(1).get(0).node.flag = true;
//		etree.tree.get(2).get(0).node.flag = true;
//		etree.tree.get(5).get(0).node.flag = true;
		/*end*/
		etree.countroot(etree.root);
		
		ReadDatabase rdb = new ReadDatabase();
//		rdb.connectDatabase();
		
		ExInfoForJson ex = rdb.readExInfo(evaluateId);
		ArrayList<VertexForJson> v = new ArrayList<VertexForJson>();
		ArrayList<EdgeForJson> e = new ArrayList<EdgeForJson>();
		
		for(int i = 0; i < etree.tree.size(); i++){
			VertexForJson vv = new VertexForJson();
			vv.id = etree.tree.get(i).get(0).node.id;
			vv.name = etree.tree.get(i).get(0).node.name;
			vv.type = etree.tree.get(i).get(0).node.vertextype;
			if(vv.type.equals("0"))
				vv.sScore = etree.tree.get(i).get(0).node.level.toString();
			else
				vv.sScore = 
				etree.tree.get(i).get(0).node.level.toString() 
				+ "/" 
				+ etree.tree.get(i).get(0).node.sumOfLevel.toString();
			vv.dScore = etree.tree.get(i).get(0).node.value;
			
			v.add(vv);
		}
		
		ArrayList<Edge> temp = rdb.readEdge(evaluateId);
		for(int i = 0; i < temp.size(); i++){
			EdgeForJson ee = new EdgeForJson();
			ee.child = temp.get(i).child;
			ee.parent = temp.get(i).parent;
			ee.weight = temp.get(i).weight;
			
			e.add(ee);
		}
				

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", JsonStr.result);
		map.put("message", JsonStr.message);
		map.put("experimentInfo", ex);
		
		Map<String, Object> mapv = new HashMap<String, Object>();
		mapv.put("vertex", v);
		
		Map<String, Object> mape = new HashMap<String, Object>();
		mape.put("edge", e);

		
		ObjectMapper mapper = new ObjectMapper();
		String result1 = mapper.writeValueAsString(map);
		String result2 = mapper.writeValueAsString(mapv);
		String result3 = mapper.writeValueAsString(mape);
		
		result1 = result1.substring(0, result1.length()-1) + ",";
		result2 = result2.substring(1, result2.length()-1) + ",";
		result3 = result3.substring(1, result3.length());
		
		result = result1 + result2 + result3;
		
		return result;
	}

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException, IOException, ParseException {
		// TODO Auto-generated method stub
		TreeToJson tr = new TreeToJson();
		System.out.println(tr.toJson("001"));
	}

}
