package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import weaponEvaluation.WeaponEvaluation;
import weaponEvaluation.WeaponType;

public class TrojanProcess extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//		Êý¾Ý¶ÁÈ¡
		String json = "{\"id\":123, \"name\":\"Test1\",\"weaponType\":\"Trojan\","
				+ "\"weaponName\":\"IE\",\"tester\":\"jason\",\"time\":\"2015-11-23 11:16:20\","
				+ " \"cntOfNode\":44, \"cntOfFilePullable\":10, \"cntOfFilePushable\":10,"
				+ " \"cntOfFileModifiable\":3, \"cntOfDataReceivable\":10, \"cntOfDataSendable\":10, "
				+ "\"isAbnormalProcessStarting\":false, \"isAbnormalExecutableFileExistent\":false, "
				+ "\"isAbnormalDLLBeingInjected\":true, \"isAbnormalDLLFileExistent\":false, "
				+ "\"isAbnormalStartupBeingAddedInUsermode\":false, "
				+ "\"isAbnormalStartupExistentInUsermode\":false,"
				+ " \"isAbnormalStartupBeingAddedInKernelmode\":false, "
				+ "\"isAbnormalStartupExistentInKernelmode\":true, \"isAbnormalServiceBeingAdded\":true, "
				+ "\"isAbnormalServiceExistent\":false, \"isAunthorizedBehaviorAccomplished\":true, "
				+ "\"isUnauthorizedBehaviorAccomplished\":false, \"cntOfBehaviorAccomplished\":12, "
				+ "\"tmOfBehaviorAccomplished\":1.8024, \"maxAvgTmOfBehaviorAccomplished\":0.3732, "
				+ "\"minAvgTmOfBehaviorAccomplished\":0.0736}";
		
		String result=WeaponEvaluation.weaponEvaluation(WeaponType.Trojan,json);
		response.getOutputStream().write(result.getBytes("utf-8"));
//		request.getRequestDispatcher("/TrojanEvaluation.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);

	}

}
