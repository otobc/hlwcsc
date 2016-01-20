package db;

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

	public static Connection getConnection()
	{
		return connection;
	}

	public static void closeConnection()
	{
		try
		{
			connection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
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

		try
		{
			Class.forName(info[0]);
			connection = DriverManager.getConnection(info[1], info[2], info[3]);
		} catch (ClassNotFoundException | SQLException e)
		{
			e.printStackTrace();
		}
		System.out.println("connect successfully");
		return connection;
	}

}
