package dboperation;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDemo
{

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException
	{
		Connection connection = DBConnect.connectMySQL("root", "123456");
		System.out.println("connect successfully");
		
		connection.close();
	}

}
