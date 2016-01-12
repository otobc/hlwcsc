package newEvaluatioin;

import java.io.UnsupportedEncodingException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;



public class ReadDatabase {
	
	private Connection conn = null;
	private Statement statement = null;
	public ArrayList<Edge> edges = new ArrayList<Edge>();
	public ArrayList<Node> nodes = new ArrayList<Node>();
	

	public void connectDatabase() {
		// TODO Auto-generated method stub

		//驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL要指向的访问数据库名myfirstdb
		String URL = "jdbc:mysql://localhost:3306/wpevalue";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的用户密码
		String password = "123456";
		
		
		try{
			// 加载驱动程序
			Class.forName(driver);
			// 连接数据库
			conn = DriverManager.getConnection(URL, user, password);
			if(!conn.isClosed())
				System.out.println("Succeeded connecting to the Database.");
			
			// statement用来执行SQL语句
			statement = conn.createStatement();

		}catch(ClassNotFoundException e){
			System.out.println("Sorry,cannot find the Driver!"); 
            e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e) {
            e.printStackTrace();
        }
	}
	
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public void read() throws SQLException, UnsupportedEncodingException{
		String sql1 = "select * from EDGE";
		String sql2 = "select * from VERTEX";
		
		ResultSet rs1 = statement.executeQuery(sql1);		
		while(rs1.next()){
			Edge e = new Edge();
			e.evaluateid = rs1.getString("evaluateid");
			e.child = rs1.getString("child");
			e.parent = rs1.getString("parent");
			e.weight = rs1.getDouble("weight");
			e.weighting = new String(e.weighting.getBytes("UTF-8"),"UTF-8");
			
			edges.add(e);
		}
		
		ResultSet rs2 = statement.executeQuery(sql2);
		while(rs2.next()){
			Node n = new Node();
			n.evaluateid = rs2.getString("evaluateid");
			n.id = rs2.getString("id");
			n.name = rs2.getString("name");
			n.nclass = rs2.getString("class");
			n.vertextype = rs2.getString("vertextype");
			n.wpindexid = rs2.getString("wpindexid");
			n.nmidix = rs2.getString("nmidix");
			n.nmidxexp = rs2.getString("nmidxexp");
			n.vertexlevel = rs2.getString("vertexlevel");
			n.nmlvl = rs2.getString("nmlvl");
			n.nmlvlexp = rs2.getString("nmlvlexp");
			n.mbsp = rs2.getString("mbsp");
			n.conclude = rs2.getString("conclude");
			
			nodes.add(n);
		}
		
		
	}
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		/*
		ReadDatabase rdb = new ReadDatabase();
		rdb.connectDatabase();
		rdb.read();
		
		System.out.println(rdb.nodes.get(1).name);
		*/
	}

}
