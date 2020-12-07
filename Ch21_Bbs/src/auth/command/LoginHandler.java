package auth.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class LoginHandler implements CommandHandler{
	
	private static final String FORM_VIEW = "/WEB-INF/view/LoginForm.jsp";
	private LoginService loginService = new LoginService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		/*
		 * request.getMethod()
		 *  - 값이 넘어오는 방식(Method) 확인 >> String return
		 *  
		 *  String.equalsIgnoreCase(String aaa)
		 *   - String의 값이 대소문자는 무시하고 aaa와 일치하는지 여부 체크
		 */
		
		if(req.getMethod().equalsIgnoreCase("GET")) { // 클라이언트로부터 GET방식으로 넘어온 경우
			
			/*
			 * Login없이 write.do를 URL창에 입력하면 필터에 의해 login.do 즉 loginHandler로 넘어온다.
			 * 이 때 넘어오는 타입은 GET방식
			 */
			System.out.println("여기는 GET방식");
			return processForm(req, res);
		} else if(req.getMethod().equalsIgnoreCase("POST")) {
			System.out.println("여기는 POST방식");
			return processSubmit(req,res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	/*
	 * Get방식으로 값이 들어온 경우 LoginForm.jsp로 돌리기 위해 사용하는 메소드
	 */
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}

	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
	
		
		/*
		 * id, password 파라미터는 어디에서 설정되서 넘어옴?
		 */
		
		String id = trim(req.getParameter("id"));
		String password = trim(req.getParameter("password"));
		
		/*
		 * 에러에 대한 정보를 담을 Map 객체 생성 후 request에 속성값으로 저장
		 */
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		if(id==null||id.isEmpty()) {
			// id가 없거나 비어있는 경우 error Map에 id 속성 저장
			errors.put("id", Boolean.TRUE);
		}
		if(password==null||password.isEmpty()) {
			// password가 없거나 비어있는 경우 error Map에 id 속성 저장
			errors.put("password", Boolean.TRUE);
		}
		if(!errors.isEmpty()) {
			/*
			 * 에러가 발생한 경우 errors 맵이 비어있지 않다.
			 * 즉, 에러맵이 비어있지 않은 경우 에러가 발생한 것이기 때문에 LoginForm으로 돌려보낸다.
			 */
			return FORM_VIEW;
		}
		/*
		 * 에러가 발생하지 않은 경우 session에 User 객체를 저장
		 */
		
		try {
			User user = loginService.login(id, password);
			req.getSession().setAttribute("authUser", user);
			res.sendRedirect(req.getContextPath()+"/index.jsp");
			
			/*
			 * request.getContextPath(): 해당 프로젝트의 Path만 가져온다.
			 */
			
			/*
			 * snedRedirect()를 했는데 return null을 하는 이유
			 * - sendRedirect는 바로 페이지로 리다이렉팅 되는 것이 아니라,
			 * 	다음에 나오는 소스들이 줄줄이 실행된다.
			 * 
			 * 즉, 여기서 return 값이 없으면 그 다음에 나오는 소스들이 제대로 실행되지 않아
			 * 에러가 발생할 수 있다.
			 */
			
			return null;
			
		} catch (LoginFailEception e) {
			errors.put("idOrPwNotMatch", Boolean.TRUE);
			return FORM_VIEW;
		}
		
	}
	
	/*
	 * 파라미터로 들어온 String이 null인 경우 그대로, 아닌 경우 trim하여 return
	 */
	private String trim(String str) {
		return str == null? null:str.trim();
	}
	
}
