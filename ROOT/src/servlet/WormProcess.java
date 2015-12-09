package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaponEvaluation.WeaponEvaluation;
import weaponEvaluation.WeaponType;


public class WormProcess extends HttpServlet {
	String json="{\"id\":456, \"name\":\"WormTest1\",\"weaponType\":\"Worm\","
			+ "\"weaponName\":\"Nimaya\",\"tester\":\"Polly\",\"time\":\"2015-11-23 11:16:20\""
			+ ",\"timeOfSaturation\":37,\"timeOfPeak\":37,\"sumOfNumber\":300,"
			+ "\"infectedNumberOfSaturation\":214,\"infectedNumberOfPeak\":214,\"timeStep\":5,"
			+ "\"maxOfNewInfectedNumber\":97,\"sumOfTraffic\":296032,\"infectedTraffic\":151996}";	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String result=WeaponEvaluation.weaponEvaluation(WeaponType.Worm,json);
		response.getOutputStream().write(result.getBytes("utf-8"));
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);

	}

}
