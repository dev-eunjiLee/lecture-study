package auth.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import auth.service.LoginFailEception;
import auth.service.LoginService;
import auth.service.User;
import member.service.JoinService;
import mvc.command.CommandHandler;

/*
 * - get방식 요청 > 폼을 위한 뷰를 리턴
 * - post방식 요청 > LoginService를 이용해서 로그인 처리
 * 
 * 왜 get일 때 post일 때를 저렇게 나누는거지?
 */

public class LogoutHandler implements CommandHandler{
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		
		HttpSession session = req.getSession();
		
		if(session!=null) {
			
			/*
			 * HttpSession.invalidate()
			 * - session을 무효화 시킨다.
			 * - session이 무효화되면 세션에 저장된 authUser 속성도 함께 삭제되며 로그아웃 처리된다.
			 * - index.jsp에서 authUser의 empty여부로 로그인 여부를 판단했었다.
			 */
			
			session.invalidate();
			
		}
		
		/*
		 * session을 무효화 시킨후 index.jsp로 돌아간다. 
		 */
		
		res.sendRedirect(req.getContextPath()+"/index.jsp");
		return null;
		
	}
	
}
