package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQL
{
	private Connection	connection	= DBConnect.getConnection();

	public ResultSet getExecuteQueryResult(String executeQuerySQL,
			String begin, String count)
	{
		ResultSet resultSet = null;
		try
		{
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(executeQuerySQL);
			//x,y
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultSet;
	}

	public ResultSet getExecuteQueryResult(String executeQuerySQL)
	{
		ResultSet resultSet = null;
		try
		{
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(executeQuerySQL);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultSet;
	}

	public int getExecuteUpdateResult(String executeUpdateSQL)
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
