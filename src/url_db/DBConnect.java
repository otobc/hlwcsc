package url_db;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBConnect
{
	private static final Connection	connection	= new DBConnect().connect();

	private DBConnect()
	{
	}

	public static Connection getConnection()
	{
		return connection;// connection.close();//待处理
	}

	private Connection connect()
	{
		Connection connection = null;

		String path = null;
		try
		{
			path = DBConnect.class.getClassLoader()
					.getResource("../../configfile/database.info").toURI()
					.getPath();
		} catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		File dbInfoFile = new File(path);
		Scanner input = null;
		try
		{
			input = new Scanner(dbInfoFile);
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		String dbinfo = input.nextLine();
		input.close();
		System.out.println("\n" + dbinfo);

		String[] info = dbinfo.split(",");
		String type = info[0].toLowerCase();
		switch (type)
		{
		case "mysql":
			connection = connectMySQL(info);
			break;
		case "sqlite":
			connection = connectSqlite(info);
			break;
		case "oracle":
			connection = connectSqlite(info);
			break;
		case "sqlserver":
			connection = connectSqlite(info);
			break;
		default:
			System.out.println("/configfile/database.info 内容格式错误！");
		}
		return connection;
	}

	/***********************************************************/
	// connect mysql
	private Connection connectMySQL(String[] info)
	{
		String databaseURL = info[1];
		String user = info[2];
		String passwd = info[3];
		Connection connection = null;
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(databaseURL
					+ "?allowMultiQueries=true", user, passwd);
		} catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("connect mysql successfully");
		return connection;
	}

	/***********************************************************/
	// connect sqlite
	private Connection connectSqlite(String[] info)
	{
		return null;

	}

	/*************************************************************/
	// connect oracle
	private Connection connectOracle(String[] info)
	{
		return null;
	}

	/*************************************************************/
	// connect sql server
	private Connection connectSqlServer(String[] info)
	{
		return null;
	}
}
