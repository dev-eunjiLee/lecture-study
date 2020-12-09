package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*
 * 데이터베이스 작업을 할 때 반복적으로 수행해야 하는 작업을 정의하는 클래스
 * getConnection, close, ...
 */
public class JdbcUtil {
	
	/*META-INF/context.xml을 이용해 ConnectionPool 사용*/
	static DataSource ds;
	
	public static Connection getConnection() {
		Connection conn = null;
			
		try {
			
			/*
			 * JNDI(Java Naiming and Direcotory Interface)
			 * - 필요한 자원을 키/값의 쌍으로 저장한 후 필요할 때 키를 이용해 값을 얻는 방법
			 * - 대표적 사용 예시: ConnectionPool
			 * 
			 * InitialContext()
			 * - 웹 어플리케이션이 처음으로 배치될 때 설정
			 * 
			 * java:comp/env: JDNI resource의 이름
			 * >> 즉 그 뒤 jdbc/OracleDB가 context.xml에서 지정해준 Resource이름이다.
			 * 
			 * DataSource
			 * - ConnectionPool을 지원하기 위한 인터페이스
			 * - DriverManager보다 향상된 기능 지원
			 * 
			 */
			
			Context initCtx = new InitialContext();
			ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/OracleDB");

			conn = ds.getConnection();
			
			System.out.println("Connection에 성공했습니다.");
			return conn;
			
		} catch(NamingException | SQLException e) {
			e.printStackTrace();
			System.out.println("Connection에 실패하였습니다.");
		}
		return null;
	}
	
	public static void close(Connection conn) {
		try {
			conn.close();
		} catch(SQLException e) {
			System.out.println("conn close 실패");
		}
		
	}
	
	public static void close(Connection conn, PreparedStatement pstmt) {
		try {
			conn.close();
			pstmt.close();
		} catch(SQLException e) {
			System.out.println("conn, pstmt close 실패");
		}
	}
	
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		try {
			conn.close();
			pstmt.close();
			rs.close();
		} catch(SQLException e) {
			System.out.println("conn, pstmt close 실패");
		}
	}
	
	public static void rollback(Connection conn) {
		try {
			conn.rollback();
		} catch(SQLException e) {
			System.out.println("rollback 실패");
		}
	}
}
