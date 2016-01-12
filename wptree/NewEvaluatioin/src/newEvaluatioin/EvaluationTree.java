package newEvaluatioin;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;

public class EvaluationTree {
	public  ArrayList<ArrayList<EdgeNode>> tree = new ArrayList<ArrayList<EdgeNode>>();
	public  Node root;	//���ڵ㣬Ч������
	
	
	public void countroot(Node node){
//		�ҵ����ڵ�v��ArrayList�е�λ��
		int i = 0;
		while(tree.get(i).get(0).node != node)
			i++;
		
		int j = 1;
		while(j < tree.get(i).size()){
			if(tree.get(i).get(j).node.value == -1){
				countroot(tree.get(i).get(j).node);
			}
			j++;
		}
		
//		�ۺϹ�ʽд������
		for(int k = 1; k < tree.get(i).size(); k++){
			tree.get(i).get(0).node.value += tree.get(i).get(k).node.value*tree.get(i).get(k).weight;
		}
		tree.get(i).get(0).node.value += 1;
	}
	
//	��������������������ʼ��(�������ݿ⸳ֵ)
	public void initial(ArrayList<ArrayList<EdgeNode>> tree) throws SQLException, UnsupportedEncodingException{
		ReadDatabase rdb = new ReadDatabase();
		rdb.connectDatabase();
		rdb.read();
		
		
//		�ҵ����ڵ㣬��ȷ�ϸ��ڵ��Ƿ�Ψһ
		int numOfRoot = 0;
		for(int i = 0; i < rdb.nodes.size(); i++){
			if(rdb.nodes.get(i).nclass.equals("0")){
				numOfRoot++;
				root = rdb.nodes.get(i);
			}
		}
		if(numOfRoot != 1){
			System.out.println("Number of root error! -" + numOfRoot);
			return;
		}
		
//		�����������ڵ��ArrayList
		for(int i = 0; i < rdb.nodes.size(); i++){
			ArrayList<EdgeNode> list = new ArrayList<EdgeNode>();
			EdgeNode enode = new EdgeNode();
			enode.node = rdb.nodes.get(i);
			list.add(enode);
			tree.add(list);
		}
		
		/*���ݴ����ݿ��ж�ȡ�ı���Ϣ����������tree*/
		for(int i = 0; i < rdb.edges.size(); i++){
			
//			Ѱ�ұ߶�Ӧ��parent��tree�е�λ��(�ĸ�list)
			int pflag = 0;
			while(pflag < tree.size()){
				if(rdb.edges.get(i).parent.equals(tree.get(pflag).get(0).node.id))
					break;
				pflag++;
			}
			
//			Ѱ�ұ߶�Ӧ��child
			int cflag = 0;
			while(cflag < rdb.nodes.size()){
				if(rdb.edges.get(i).child.equals(rdb.nodes.get(cflag).id))
					break;
				cflag++;
			}
			
			EdgeNode enode = new EdgeNode();
			rdb.nodes.get(cflag).indegree++;
			rdb.nodes.get(pflag).outdegree++;
			enode.node = rdb.nodes.get(cflag);
			enode.weight = rdb.edges.get(i).weight;
			
			tree.get(pflag).add(enode);
		}
		/*�������ڲ��ԣ���δ�����ݿ�����*/
		/*
		Node[] node = new Node[6];
		for(int i = 0; i < 6; i++){
			node[i] = new Node();
		}
		root = node[0];
		
		for(int i = 0; i < 3; i++){
			node[i].value = -1;
		}
		node[3].value = 3;
		node[4].value = 4;
		node[5].value = 5;
		
		EdgeNode[] v = new EdgeNode[12];
		for(int i = 0; i < 12; i++){
			v[i] = new EdgeNode();
		}
		for(int i = 0; i < 6; i++){
			v[i].node = node[i];
		}
		v[6].node = node[1];
		v[7].node = node[2];
		v[8].node = node[3];
		v[9].node = node[4];
		v[10].node = node[4];
		v[11].node = node[5];
		
		ArrayList<EdgeNode> list0 = new ArrayList<EdgeNode>();
		ArrayList<EdgeNode> list1 = new ArrayList<EdgeNode>();
		ArrayList<EdgeNode> list2 = new ArrayList<EdgeNode>();
		ArrayList<EdgeNode> list3 = new ArrayList<EdgeNode>();
		ArrayList<EdgeNode> list4 = new ArrayList<EdgeNode>();
		ArrayList<EdgeNode> list5 = new ArrayList<EdgeNode>();
		
		list0.add(v[0]);
		list0.add(v[6]);
		list0.add(v[7]);
		list1.add(v[1]);
		list1.add(v[8]);
		list1.add(v[9]);
		list2.add(v[2]);
		list2.add(v[10]);
		list2.add(v[11]);
		list3.add(v[3]);
		list4.add(v[4]);
		list5.add(v[5]);
		
		tree.add(list0);
		tree.add(list1);
		tree.add(list2);
		tree.add(list3);
		tree.add(list4);
		tree.add(list5);
		*/
		
	}
	public static void main(String[] args) throws SQLException, UnsupportedEncodingException {
		/*
		initial(tree);
		
		tree.get(3).get(0).node.value = 2;
		tree.get(4).get(0).node.value = 4;
		tree.get(5).get(0).node.value = 6;
		countroot(root);
		
		for(int i = 0; i < tree.size(); i++)
			System.out.println(tree.get(i).get(0).node.value);
		*/
		
	}
}
