package dboperation;

import java.sql.Connection;
import java.sql.SQLException;

public class TestDemo
{

	public static void main(String[] args)
			throws ClassNotFoundException, SQLException
	{
		Connection connection = DBConnect.connectMySQL("root", "123456",
				"evaluation_config");
		System.out.println("connect successfully");

		DBCreateTable createTable = new DBCreateTable(connection);
		createTable.initDatabase();
		System.out.println("init database successfully");

	}

}
