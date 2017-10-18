package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import url.URLtoSQL;
import bean_json.BeanToJson;
import bean_json.InsDtBean;
import db.ExecuteSQL;

public class Insert extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		// 1.��ȡǰ���������
		System.out.println("\n*********get insert request");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}
		ObjectMapper mapper = new ObjectMapper();
		InsDtBean insdata = mapper.readValue(sb.toString(), InsDtBean.class);
		String table = insdata.table;
		String data = insdata.data;
		System.out.println("table=" + table + "\ndata=" + data);

		// 2.�Բ�������ƴ�ӳ�SQL���
		URLtoSQL urltoSQL = new URLtoSQL();
		String insertsql = urltoSQL.getInsertSQLFromURL(table, data);
		System.out.println("insertsql=" + insertsql);

		// 3.�������ݿ�ִ�еõ����
		int result = ExecuteSQL.getExecuteUpdateResult(insertsql);

		// 4.��SQLִ�н�����д���ӳ��Ϊjson��Ӧ��
		BeanToJson resultJson = new BeanToJson();
		String insertJson = resultJson.getInsertJson(result);
		System.out.println("insertJson=" + insertJson);

		// 5.����json��ǰ��
		response.getOutputStream().write(insertJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

}
