package jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	
	/*
	 * jdbc.DBCPInitListenr에서 만든 ConnectionPool을 가져오는 메소드
	 * return값으로 ConnectionPool에서 커넥션을 1개를 전달한다.
	 */
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:apache:commons:dbcp:board");
	}

}
