package newEvaluatioin;

import java.io.UnsupportedEncodingException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.wltea.expression.ExpressionEvaluator;
import org.wltea.expression.datameta.Variable;
import org.wltea.expression.datameta.BaseDataMeta.DataType;

import db.DBConnect;
import db.ExecuteSQL;
import treeToJson.ExInfoForJson;



public class ReadDatabase {
	
	private static Connection conn = DBConnect.getConnection();
//	private Statement statement = conn.createStatement();

//	public void connectDatabase() {
//		// TODO Auto-generated method stub
//
//		//驱动程序名
//		String driver = "com.mysql.jdbc.Driver";
//		// URL要指向的访问数据库名
//		String URL = "jdbc:mysql://localhost:3306/wpevalue";
//		// MySQL配置时的用户名
//		String user = "root";
//		// MySQL配置时的用户密码
//		String password = "123456";
//		
//		
//		try{
//			// 加载驱动程序
//			Class.forName(driver);
//			// 连接数据库
//			conn = DriverManager.getConnection(URL, user, password);
//			if(!conn.isClosed())
//				System.out.println("Succeeded connecting to the Database.");
//			
//			// statement用来执行SQL语句
//			statement = conn.createStatement();
//
//		}catch(ClassNotFoundException e){
//			System.out.println("Sorry,cannot find the Driver!"); 
//            e.printStackTrace();
//		}catch(SQLException e){
//			e.printStackTrace();
//		}catch(Exception e) {
//            e.printStackTrace();
//        }
//	}
//	
//	public void close(){
//		try {
//			conn.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}	
	
	
	/**从数据库指标缓存表得到指标值*/
	public Double getValueOfIndex(String evaluateid, String wpindexid, String expid) throws SQLException{
		Double result = new Double(0.0d);
		String sql = 
				"select value from WPIDXCACHE where evaluateid=" + evaluateid + " and wpindexid=" + wpindexid + " and experimentid=" + expid;
		ResultSet rs = ExecuteSQL.getExecuteQueryResult(sql);
		rs.next();
		result = Double.valueOf(rs.getString("value"));
		return result;
	}
	
	
	/**由原始数据求得指标值,并存入数据库指标数据缓存表
	 * @throws SQLException 
	 * @throws ParseException */
	public void computeWpindexcache(String wpindexId, String evaluateId) throws SQLException, ParseException{
		Object result = new Object();
		ReadDatabase rdb = new ReadDatabase();
//		rdb.connectDatabase();
		
//		得到指标计算表达式
		String expression = rdb.readExpOfWpindex(wpindexId);
		ExInfoForJson exInfo = rdb.readExInfo(evaluateId);
		List<Variable> var = new ArrayList<Variable>();
		var.add(new Variable("t", DataType.DATATYPE_DATE, extexp.Extexp.todate(exInfo.beginTime)));
		result = ExpressionEvaluator.evaluate(expression, var);
		
		
//		存入指标数据缓存表
		String experimentId = exInfo.id;
		
		String[] datatypeStr = result.getClass().toString().split("\\.");
		String datatype = datatypeStr[datatypeStr.length - 1];		
		switch(datatype){
		case "String":
			datatype = "0";
			break;
		case "Long":
			datatype = "1";
			break;
		case "Double":
			datatype = "2";
			break;
		case "Boolean":
			datatype = "3";
			break;
		default:
			datatype = "E";
			break;
		}
		
		String sql = 
				"insert into WPIDXCACHE values('" + evaluateId + "','" + wpindexId + "','" + experimentId + "','" + datatype + "','" + result + "');";
		
		ExecuteSQL.getExecuteUpdateResult(sql);
		
//		rdb.close();		
	}
	
	/**根据评估编号evaluationId读取试验信息表
	 * @throws SQLException */
	public ExInfoForJson readExInfo(String evaluationId) throws SQLException{
		ExInfoForJson result = new ExInfoForJson();
		String sql1 = "select expid from EVALUATE where id=" + evaluationId;
		ResultSet rs1 = ExecuteSQL.getExecuteQueryResult(sql1);
		rs1.next();
		String expid = rs1.getString("expid");
		
		String sql2 = "select * from EXPERIMENT where id=" + expid;
		ResultSet rs2 = ExecuteSQL.getExecuteQueryResult(sql2);
		rs2.next();
		result.id = rs2.getString("id");
		result.name = rs2.getString("name");
		result.weaponType = rs2.getString("wptype");
		result.tester = rs2.getString("tester");
		result.beginTime = rs2.getString("bgtime");
		result.endTime = rs2.getString("edtime");
		
		return result;		
	}
		
	/**读取评估编号为evaluationId的所有节点*/
	public ArrayList<Node> readNode(String evaluationId) throws SQLException{
		String sql = "select * from VERTEX where evaluateid=" + evaluationId;
		ArrayList<Node> result = new ArrayList<Node>();
		ResultSet rs = ExecuteSQL.getExecuteQueryResult(sql);
		while(rs.next()){
			Node n = new Node();
			n.evaluateid = rs.getString("evaluateid");
			n.id = rs.getString("id");
			n.name = rs.getString("name");
			n.nclass = rs.getString("class");
			n.vertextype = rs.getString("vertextype");
			n.wpindexid = rs.getString("wpindexid");
			n.nmidix = rs.getString("nmidx");
			n.nmidxexp = rs.getString("nmidxexp");
			n.vertexlevel = rs.getString("vertexlevel");
			n.nmlvl = rs.getString("nmlvl");
			n.nmlvlexp = rs.getString("nmlvlexp");
			n.mbsp = rs.getString("mbsp");
			n.conclude = rs.getString("conclude");
			n.weighting = rs.getString("weighting");
			
			result.add(n);
		}
		return result;
	}
	
	/**读取评估编号为evaluationId的所有边*/
	public ArrayList<Edge> readEdge(String evaluationId) throws SQLException, UnsupportedEncodingException{
//		evaluationId是评估编号
		String sql1 = "select * from EDGE where evaluateid=" + evaluationId;
		
		ArrayList<Edge> result = new ArrayList<Edge>();
		ResultSet rs1 = ExecuteSQL.getExecuteQueryResult(sql1);		
		while(rs1.next()){
			Edge e = new Edge();
			e.evaluateid = rs1.getString("evaluateid");
			e.child = rs1.getString("child");
			e.parent = rs1.getString("parent");
			e.weight = rs1.getDouble("weight");
//			e.weighting = new String(e.weighting.getBytes("UTF-8"),"UTF-8");
			
			result.add(e);
		}
		return result;
	}
	
	/**根据指标编码读取指标表达式expression
	 * 要查询的表是wpindex
	 * @throws SQLException */
	public String readExpOfWpindex(String wpindexId) throws SQLException{
		String result = new String();
		String sql = "select expression from WPINDEX where id=" + wpindexId;		
		ResultSet rs = ExecuteSQL.getExecuteQueryResult(sql);
		rs.next();
		result = rs.getString("expression");
		return result;
	}
	
	
	

	public static void main(String[] args) throws SQLException, ParseException {
		// TODO Auto-generated method stub
		
//		ReadDatabase rdb = new ReadDatabase();
//		rdb.connectDatabase();
		
//		ExInfoForJson ex = new ExInfoForJson();
//		ex = rdb.readExInfo("001");
//		rdb.countWpindexcache("003", "001");
		
//		System.out.println(rdb.getValueOfIndex("001", "003", "009"));
		

	}

}
