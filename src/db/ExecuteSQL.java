package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQL
{
	private static Connection	connection	= DBConnect.getConnection();

	public static ResultSet getExecuteQueryResult(String executeQuerySQL)
	{
		ResultSet resultSet = null;
		try
		{
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(executeQuerySQL);
			System.out.println("execute sql success");
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultSet;
	}

	public static int getExecuteUpdateResult(String executeUpdateSQL)
	{
		int result = 0;
		try
		{
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(executeUpdateSQL);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}

}
