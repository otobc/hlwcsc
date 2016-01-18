package servlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import url_db.DBConnect;
import url_db.ExecuteSQL;
import url_db.URLtoSQL;
import bean_json.BeanToJson;

public class Range extends HttpServlet
{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		// 1.��ȡǰ���������
		System.out.println("\n*********get range request");
		String table = request.getParameter("table");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String query = request.getParameter("query");
		System.out.println("table=" + table + "\nkey=" + key + "\nvalue="
				+ value + "\nquery=" + query);

		// 2.�Բ�������ƴ�ӳ�SQL���
		URLtoSQL urltoSQL = new URLtoSQL();
		String rangesql = urltoSQL.getRangeSQLFromURL(table, key, value, query);

		// 3�������ݿ�ִ�еõ����
		ExecuteSQL executeSQL = new ExecuteSQL();
		ResultSet resultSet = executeSQL.getSelectResult(rangesql);

		// 4.�Խ�����д���ӳ��Ϊjson��Ӧ��
		BeanToJson resultJson = new BeanToJson();
		String rangeJson = resultJson.getRangeJson(resultSet);
		System.out.println("rangeJson=" + rangeJson);

		// 5.����json��ǰ��
		response.getOutputStream().write(rangeJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}
}