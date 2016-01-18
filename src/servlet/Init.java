package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean_json.BeanToJson;

public class Init extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		System.out.println("\n**********get Init request");
		String table = request.getParameter("table");
		
		BeanToJson beanToJson = new BeanToJson();
		String initJson = beanToJson.getInitJson(table);
		System.out.println(table + "\n" + initJson);

		response.getOutputStream().write(initJson.getBytes("utf-8"));
		System.out.println("response successfully");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

}
