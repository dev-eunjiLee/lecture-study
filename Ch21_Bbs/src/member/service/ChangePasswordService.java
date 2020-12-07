package member.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

public class ChangePasswordService {
	
	private MemberDao memberDao = new MemberDao();
	
	public void changePassword(String userid, String curPwd, String newPwd) throws SQLException {
		Connection conn = null;
		try {
			
			/*
			 * ConnectionPool에서 커넥션 가져오기
			 */
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션 시작하지만, 커밋은 나중에 한꺼번에 하겠다!
			
			Member member = memberDao.selectById(conn, userid);
			
			if(member==null) { // 암호를 변경할 회원 데이터가 없는경우
				throw new MemberNotFoundException();
			}
			
			/*
			 * 변경되기 전 비밀번호가 맞게 입력되었는지 확인
			 * 기존 비밀번호가 제대로 입력되지 않았다면 exception 발생
			 */
			if(!member.matchPassword(curPwd)) { 
				throw new InvalidPasswordException();
			}
			/*
			 * 변경한 암호를 member객체에 넣어준 후 해당 member객체를 이용해 update진행
			 */
			member.chanagePassword(newPwd);
			memberDao.update(conn, member);
			conn.commit();
		} catch(SQLException e) {
			/*
			 * 예외가 발생한 경우 rollback처리
			 */
			jdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} finally {
			jdbcUtil.close(conn);
		}
		
	}

}
