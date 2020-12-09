package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import db.JdbcUtil;
import vo.MemberBean;

public class MemberDao {
	
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	
	/*
	 * MemberDao를 싱글톤 타입으로 제작
	 */
	
	public static MemberDao instance;
	
	public static MemberDao getInstance() {
		if(instance==null) {
			instance = new MemberDao();
		}
		return instance;
	}
	
	private MemberDao(){}
	
	
	
	/*
	 * DB 연결 설정
	 */
	
	public void setConnection(Connection con) {
		this.con = con;
	}
	
	
	
	/*
	 * ???로그인한 아이디를 리턴... 왜...?
	 */
	
	public String selectLoginId(MemberBean member) {
		
		return null;
	}
	
	/*
	 * Member 삽입하기
	 */
	
	public int insertMember(MemberBean member) {
		
		return 0;
	}

	/*
	 * ????
	 */
	
	public ArrayList<MemberBean> selectMemberList() {
		
		return null;
	}
	
	/*
	 * id값에 일치하는 member객체 가져오기
	 */
	private static final String SELECT_MEMBER_QUERY = "select * from member where MEMBER_ID=?";
	
	public MemberBean selectMember(String id) {
		try {
			pstmt = con.prepareStatement(SELECT_MEMBER_QUERY);
			pstmt.setString(1, id);
			rs =  pstmt.executeQuery();
			
			MemberBean bean = null;
			
			if(rs.next()) {
				String m_id = rs.getString("MEMBER_ID");
				String pw = rs.getString("MEMBER_PW");
				String name = rs.getString("MEMBER_NAME");
				int age = rs.getInt("MEMBER_AGE");
				String gender = rs.getString("MEMBER_GENDER");
				String email = rs.getString("MEMBER_EMAIL");
				
				bean = new MemberBean(m_id, pw, name, age, gender, email);
			}
			
			return bean;
			
		} catch(SQLException e) {
			System.out.println("===> MemberDao.selectMember() 예외 발생");
			return null;
		} finally {
			JdbcUtil.close(con, pstmt, rs);
		}
	}
	
	/*
	 * 입력한 아이디 정보 삭제
	 * - 입력한 id와 일치하는 값을 찾아서 삭제한다.
	 * - 삭제한 후 return되는 쿼리문 실행 횟수를 deleteCnt에 담아 보낸다.
	 */
	
	private static final String DELETE_QUERY = "delete from member where MEMBER_ID = ?";
	
	public int deleteMember(String id) {
		
		try {
			pstmt = con.prepareStatement(DELETE_QUERY);
			pstmt.setString(1, id);
			int deleteCnt = pstmt.executeUpdate();
			
			return deleteCnt;
		} catch(SQLException e) {
			System.out.println("===> MemberDao.deleteMember() 예외 발생");
			return 0;
		} finally {
			JdbcUtil.close(con, pstmt);
		}
	}
}
