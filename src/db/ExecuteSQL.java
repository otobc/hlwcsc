package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSQL
{
	private Connection	connection	= DBConnect.getConnection();

	public ResultSet getSelectResult(String rangesql)
	{
		ResultSet resultSet = null;
		try
		{
			Statement statement = connection.createStatement();
			resultSet = statement.executeQuery(rangesql);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return resultSet;
	}

	public int getInsertResult(String insertsql)
	{
		int result = 0;
		try
		{
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(insertsql);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public int getDeleteResult(String deletesql)
	{
		int result = 0;
		try
		{
			Statement statement = connection.createStatement();
			result = statement.executeUpdate(deletesql);
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		return result;
	}

}
