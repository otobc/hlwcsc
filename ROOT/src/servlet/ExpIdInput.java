package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ExpIdInput extends HttpServlet {
	String[][] expInfo={{"id","name","weaponType","weaponName","tester","time"},
						{"123","Test1","Trojan","Nimya","Jason","2015-11-30"},
						{"456","Test2","Worm","Panda","Polly","2015-11-30"},
						{"789","Test3","DDos","SDbot","mike","2015-11-30"}
					   };
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String id=null;
		id=request.getParameter("id");
		System.out.println(id);
		if(id==null) {
			response.setContentType("text/html;charset=utf-8");
			response.getOutputStream().write("id is null".getBytes("utf-8"));
		}
		else{
				String weaponType=null;
				for(int i=0;i<expInfo.length;i++){
						if(id.equals(expInfo[i][0])){
							weaponType=expInfo[i][2];
						}
					}
				if(weaponType==null){
					response.getOutputStream()
						    .write("{\"0\":\"没有此Id，请输入正确Id\"}".getBytes("utf-8"));
				}
				else if(weaponType.equals("DDos")) {
					request.getRequestDispatcher("/servlet/DDosProcess")
						   .forward(request, response);
				}
				else if(weaponType.equals("Trojan")) {
					request.getRequestDispatcher("/servlet/TrojanProcess")
						   .forward(request, response);
				}
				else if(weaponType.equals("Worm")) {
					request.getRequestDispatcher("/servlet/WormProcess")
						   .forward(request, response);
				}
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		doGet(request, response);
	}

}
