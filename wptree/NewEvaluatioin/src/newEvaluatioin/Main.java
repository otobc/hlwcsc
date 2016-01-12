package newEvaluatioin;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		EvaluationTree etree = new EvaluationTree();
		etree.initial(etree.tree);
		
		/*给叶节点赋值，暂时用于测试*/
		etree.tree.get(3).get(0).node.value = 2;
		etree.tree.get(4).get(0).node.value = 4;
		etree.tree.get(5).get(0).node.value = 6;
		
		etree.countroot(etree.root);
		
		for(int i = 0; i < etree.tree.size(); i++)
			System.out.println(etree.tree.get(i).get(0).node.value);
	}

}
