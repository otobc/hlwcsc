package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import treeToJson.TreeToJson;
import bean_json.EvaluateBean;

public class Evaluate extends HttpServlet
{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String evaluateId = request.getParameter("id");
		String isReadCache = request.getParameter("isReadCache");
		System.out.println("evaluateId="+evaluateId+"\nisReadCache="+isReadCache);
		
		EvaluateBean.evaluateId=evaluateId;
		EvaluateBean.isReadCache=isReadCache;
		
		TreeToJson tr = new TreeToJson();
		String evaluateJson="";
		try
		{
			evaluateJson=tr.toJson(evaluateId);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | SQLException | ParseException e)
		{
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(evaluateJson);
		
		response.getOutputStream().write(evaluateJson.getBytes("utf-8"));
		System.out.println("response successfully");		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		doGet(request, response);
	}

}
