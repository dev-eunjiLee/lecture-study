package auth.service;

import java.sql.Connection;
import java.sql.SQLException;

import jdbc.connection.ConnectionProvider;
import member.dao.MemberDao;
import member.model.Member;

/*
 * MemberDao를 이용해서 아이디에 해당하는 회원 데이터가 존재하는지 확인
 * 
 * -X: 회원 데이터가 존재하지않거나 암호가 일치하지 않으면 LoginFilException발생
 * -O: 암호가 일치하면회원 아이디와 이름을 담은 User 객체 생성 후 리턴
 */


public class LoginService {
	
	/*
	 * MemberDao를 private으로 구현한 이유?
	 */

	private MemberDao memberDao = new MemberDao();
	
	public User login(String id, String password) {
		
		/*
		 * try - with - resource
		 */
		
		try(Connection conn = ConnectionProvider.getConnection()){
			
			/*
			 * 입력한 ID와 일치하는 ID를 가진 데이터를 오라클 데이터베이스에서 뽑아와 member에 저장
			 */
			
			Member member = memberDao.selectById(conn, id);
			
			if(member==null) {
				
				/*
				 * member객체가 없는 경우 > 일치하는 id가 없는 경우 >> 로그인 실패 예외 처리
				 */
				
				throw new LoginFailEception();
			}
			if(!member.matchPassword(password)) {
				
				/*
				 * 일치하는 ID값에 해당하는 데이터 값을 가ㄴ져왔지만,
				 * 해당 데이터값에 입력된 패스워드와 클라이언트가 입력한 패스워드가 다른 경우
				 */
				
				throw new LoginFailEception();
			}
			
			/*
			 * ID,PW가 모두 일치하는 데이터 값이 있는 경우,
			 * 해당 데이터에서 ID,PW를 User객체에 저장하여 리턴
			 */
			
			return new User(member.getId(), member.getName());
			
		}catch(SQLException e) {
			
			/*
			 * 발생한 예외들 다 잡아서 RuntimeException으로 위로 던져버린다.
			 */
			
			throw new RuntimeException(e);
		}

	}
	
}
