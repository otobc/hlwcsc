package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect
{
	static String className = null;
	static String jdbcUrl = null;
	static String dbUser = null;
	static String dbPwd = null;
	
	public static void setDbInfo() 
	{
		// TODO
		className = "org.sqlite.JDBC";
		jdbcUrl = "jdbc:sqlite:E:\\workspace\\tmp\\evaluation_config.db";
		dbUser = "user";
		dbPwd = "password";
	}
	
	public static Connection connect() throws ClassNotFoundException, SQLException
	{
		Class.forName(className);
		Connection connection = DriverManager
				.getConnection(jdbcUrl, dbUser, dbPwd);
		return connection;
	}

}
