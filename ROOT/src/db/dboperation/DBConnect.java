package dboperation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect
{
	/*************************************************************/
	// connect mysql
	public static Connection connectMySQL(String user, String passwd)
			throws SQLException, ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager
				.getConnection("jdbc:mysql://localhost/evaluation_config"
						+ "?allowMultiQueries=true", user, passwd);
		return connection;
	}

	/*************************************************************/
	// connect sqlite

	/*************************************************************/
	// connect oracle

	/*************************************************************/
	// connect sql server

}
