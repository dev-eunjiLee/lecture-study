package member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import jdbc.jdbcUtil;
import member.model.Member;

public class MemberDao {
	
	
	/*
	 * 파라미터로 입력한 id값과 일치하는 데이터를 오라클데이터베이스에서 Member 객체에 담는다.
	 */
	
	public Member selectById(Connection conn, String id) throws SQLException {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(
					"select * from member where memberid = ?");
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			Member member = null;
			
			if(rs.next()) {
				
				/*
				 * 오라클의 Date 가져올 때
				 * - ResultSet.getDate():: java.sql.Date >> 연도, 월, 일까지만 설정되어 있다.
				 * - ResultSet.getTimeStamp():: java.sql.TimeStamp >> java.util.Date 와 동일한형태
				 */
				
				member = new Member(rs.getString("memberid"),
									rs.getString("name"),
									rs.getString("password"),
									toDate(rs.getTimestamp("regdate")));	
			} return member;

		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(pstmt);
		}
		
		
	}
	
	/*
	 * 오라클 데이터베이스에서 받아온 날짜 값을 Date 객체로 바꿔준다.
	 */
	
	private Date toDate(Timestamp date) {		
		return date==null?null:new Date(date.getTime());
		
		/*
		 * Timestamp.getTime() >> long 타입으로 날짜 리턴
		 * Date(long date) >> long타입으로 들어온 값을 받아 Date 객체 생성
		 * 
		 */
	}
	
	
	
	/*
	 * Member 객체에 들어있는 값을 오라클 데이터베이스로 보낸다.
	 */
	
	public void insert(Connection conn, Member mem) throws SQLException {
		try(PreparedStatement pstmt =
				conn.prepareStatement("insert into MEMBER values(?,?,?,?)")){
			pstmt.setString(1, mem.getId());
			pstmt.setString(2, mem.getName());
			pstmt.setString(3, mem.getPassword());
			pstmt.setTimestamp(4, new Timestamp(mem.getRegDate().getTime()));
			
			/*
			 * Date.getTime()
			 * 현재 시간을 long타입으로 리턴한다.
			 */
			
			pstmt.executeUpdate();
		}
	}
	
	/*
	 * memberid값이 같은 경우 입력된 오라클 데이터베이스에서 name,password를 수정한다.
	 */
	
	public void update(Connection conn, Member member) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement(
				"update member set name=?, password=? where memberid=?")){
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getId());
			pstmt.executeUpdate();
		}
		
	}
	
}
