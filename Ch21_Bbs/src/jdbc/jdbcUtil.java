package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.rowset.serial.SerialException;

// 434P 간단한 close() 및 rollback() 처리 코드를 위한 클래스
public class jdbcUtil {
	
	public static void close(ResultSet rs) {
		if(rs!=null) {// rs에 내용이 있는 경우
			try {
				rs.close();
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static void close(Statement stmt) {
		if(stmt!=null) {// stmt에 내용이 있는 경우
			try {
				stmt.close();
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		if(pstmt!=null) {// pstmt에 내용이 있는 경우
			try {
				pstmt.close();
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static void close(Connection conn) {
		if(conn!=null) {// conn에 내용이 있는 경우
			try {
				conn.close(); // 커넥션 풀로 반환
			} catch (SQLException e) {
				
			}
		}
	}
	
	public static void rollback(Connection conn) {
		if(conn!=null) {// conn에 내용이 있는 경우
			try {
				conn.rollback();
			} catch (SQLException e) {
				
			}
		}
	}

}
