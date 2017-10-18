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
import bean_json.DelDtBean;
import db.ExecuteSQL;

public class Delete extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("\n*********get delete request");

		// 1.��ȡǰ���������
		BufferedReader br = new BufferedReader(new InputStreamReader(
				request.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null)
		{
			sb.append(line);
		}
		ObjectMapper mapper = new ObjectMapper();
		DelDtBean deldata = mapper.readValue(sb.toString(), DelDtBean.class);
		String table = deldata.table;
		String query = deldata.query;
		System.out.println("table=" + table + "\nquery=" + query);

		// 2.�Բ�������ƴ�ӳ�SQL���
		URLtoSQL urltoSQL = new URLtoSQL();
		String deletesql = urltoSQL.getDeleteSQLFromURL(table, query);
		System.out.println("deletesql=" + deletesql);

		// 3.�������ݿ�ִ�еõ����
		int result = ExecuteSQL.getExecuteUpdateResult(deletesql);

		// 4.��SQLִ�н�����д���ӳ��Ϊjson��Ӧ��
		BeanToJson resultJson = new BeanToJson();
		String deleteJson = resultJson.getDeleteJson(result);
		System.out.println("deleteJson=" + deleteJson);

		// 5.����json��ǰ��
		response.getOutputStream().write(deleteJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

}
