package db;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDemo
{

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException
	{
		DBConnect.setDbInfo();	
		Connection connection = DBConnect.connect();
		System.out.println("connect successfully");
		
		connection.close();
	}

}
