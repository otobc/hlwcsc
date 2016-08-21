package newEvaluation;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {

	public static void main(String[] args) throws SQLException, UnsupportedEncodingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ParseException, NoSuchMethodException, SecurityException {
		// TODO Auto-generated method stub
		EvaluationTree etree = new EvaluationTree();
		etree.initial(etree.tree, "001");
		
		/*给叶节点赋值，暂时用于测试*/
		etree.tree.get(3).get(0).node.value = new Double(2.0d);
		etree.tree.get(4).get(0).node.value = new Double(4.0d);
		etree.tree.get(5).get(0).node.value = new Double(6.0d);
		etree.tree.get(3).get(0).node.flag = true;
		etree.tree.get(4).get(0).node.flag = true;
		etree.tree.get(5).get(0).node.flag = true;
		
		etree.countroot(etree.root);
		
		for(int i = 0; i < etree.tree.size(); i++)
			System.out.println(etree.tree.get(i).get(0).node.value);
	}

}
