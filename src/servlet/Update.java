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
import bean_json.UptDtBean;
import db.ExecuteSQL;

public class Update extends HttpServlet
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
		System.out.println("\n*********get update request");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}
		ObjectMapper mapper = new ObjectMapper();
		UptDtBean uptdata = mapper.readValue(sb.toString(), UptDtBean.class);
		String table = uptdata.table;
		String data = uptdata.data;
		String query = uptdata.query;
		System.out.println("table=" + table + "\ndata=" + data + "\nquery"
				+ query);

		// 2.�Բ�������ƴ�ӳ�SQL���
		URLtoSQL urltoSQL = new URLtoSQL();
		String updatesql = urltoSQL.getUpdateSQLFromURL(table, data, query);
		System.out.println("updatesql=" + updatesql);

		// 3.�������ݿ�ִ�еõ����
		int result = ExecuteSQL.getExecuteUpdateResult(updatesql);

		// 4.��SQLִ�н�����д���ӳ��Ϊjson��Ӧ��
		BeanToJson resultJson = new BeanToJson();
		String updateJson = resultJson.getUpdateJson(result);
		System.out.println("updateJson=" + updateJson);

		// 5.����json��ǰ��
		response.getOutputStream().write(updateJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

}
