package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaponEvaluation.WeaponEvaluation;
import weaponEvaluation.WeaponType;

public class DDosProcess extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String json = "{\"id\":789, \"name\":\"Test3\",\"weaponType\":\"DDos\","
				+ "\"weaponName\":\"SDbot\",\"tester\":\"mike\",\"time\":\"2015-11-30 11:16:20\","
				+ " \"availableMachine\":10, \"totalMachine\":16, \"availableMachineIncreaseRate\":0.1,"
				+ " \"isMachineInDifferenceNet\":true, \"isMachineIpFake\":false, \"masterServerLevel\":2, "
				+ "\"botnetMachineAverageCpu\":0.5, \"botnetMachineAverageMemery\":0.6, "
				+ "\"ddosTime\":100, \"ddosPacket\":4096, "
				+ "\"ddosSendPacketSpace\":0.01, "
				+ "\"serverToDeadNum\":7,"
				+ " \"attackServerNum\":10}";
		String result=WeaponEvaluation.weaponEvaluation(WeaponType.DDos,json);
		response.getOutputStream().write(result.getBytes("utf-8"));
//		request.getRequestDispatcher("/DDosEvaluation.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		doGet(request, response);
	}

}
