package member.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

/*
 * 회원 가입
 */

public class JoinService {
	
	private MemberDao memberDao = new MemberDao();
	
	public void join(JoinRequest joinReq) {
		Connection conn = null;
		
		try {
			conn = ConnectionProvider.getConnection(); // 커넥션 풀에서 커넥션 가져오기
			conn.setAutoCommit(false); // 자동 commit X
			
			// JoinRequest에 있는 Id값과 일치하는 데이터를 Member 객체에 넣기
			Member member = memberDao.selectById(conn, joinReq.getId());
			
			/*
			 * 값이 null이 아니라는건 해당 id로 생성된 계정이 있다는 이야기이기 때문에
			 * rollback하고 DupicatedIdException 예외를 던진다. 그 후 바로 finally로 넘어간다.
			 */
			if(member != null) {
				jdbcUtil.rollback(conn);
				throw new DuplicatedIdException();
			}
			
			/*
			 * joinReq를 통해 넘어온 Id가 데이터에 없는 경우 입력 받은 내용을 오라클 insert
			 * >> 계정 생성 
			 */
			memberDao.insert(conn, new Member(joinReq.getId(),
											  joinReq.getName(),
											  joinReq.getPassword(),
											  new Date())
					);
			
			conn.commit();
		} catch(SQLException e) {
			
			/*
			 * 기타 SQLException 발생할 경우 rollback
			 */
			
			jdbcUtil.rollback(conn);
			throw new RuntimeException();
			
		} finally {
			jdbcUtil.close(conn); // 커넥션풀로반환
		}
	}

}
