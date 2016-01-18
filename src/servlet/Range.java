package servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean_json.BeanToJson;
import db.DBConnect;
import db.ExecuteSQL;
import url.URLtoSQL;

public class Range extends HttpServlet
{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		// 1.获取前端请求参数
		System.out.println("\n*********get range request");
		String table = request.getParameter("table");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String query = request.getParameter("query");
		System.out.println("table=" + table + "\nkey=" + key + "\nvalue="
				+ value + "\nquery=" + query);

		// 2.对参数处理拼接成SQL语句
		URLtoSQL urltoSQL = new URLtoSQL();
		String rangesql = urltoSQL.getRangeSQLFromURL(table, key, value, query);

		// 3连接数据库执行得到结果
		ExecuteSQL executeSQL = new ExecuteSQL();
		ResultSet resultSet = executeSQL.getSelectResult(rangesql);

		// 4.对结果进行处理映射为json响应串
		BeanToJson resultJson = new BeanToJson();
		String rangeJson = resultJson.getRangeJson(resultSet);
		System.out.println("rangeJson=" + rangeJson);

		// 5.发送json给前端
		try {
			DBConnect.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getOutputStream().write(rangeJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}
}
