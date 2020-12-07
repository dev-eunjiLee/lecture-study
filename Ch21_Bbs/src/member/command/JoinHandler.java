package member.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import member.service.DuplicatedIdException;
import member.service.JoinRequest;
import member.service.JoinService;
import mvc.command.CommandHandler;

public class JoinHandler implements CommandHandler {
	
	private static final String FORM_VIEW = "/WEB-INF/view/joinForm.jsp";
	private JoinService joinService = new JoinService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		/*
		 * get: 가입의사 전달
		 * post: 가입 내용 저장
		 */
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			/*
			 * request.getMethod()
			 *  - 값이 넘어오는 방식(Method) 확인 >> String return
			 *  
			 *  String.equalsIgnoreCase(String aaa)
			 *   - String의 값이 대소문자는 무시하고 aaa와 일치하는지 여부 체크
			 */
			
			return processForm(req, res); // "/WEB-INF/view/joinForm.jsp"
			
		} else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		}
		
		return null;
	}
	
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		
		JoinRequest joinReq = new JoinRequest();
		
		joinReq.setId(req.getParameter("id"));
		joinReq.setName(req.getParameter("name"));
		joinReq.setPassword(req.getParameter("password"));
		joinReq.setConfirmPassword(req.getParameter("confirmPassword"));
		
		Map<String, Boolean> errors = new HashMap<String, Boolean>();
		req.setAttribute("errors", errors);
		
		joinReq.validate(errors); // 필드의값이 유효한지 검사 > 유효하지 못한 경우 해당 내용을 errors Map 객체에 담음
		
		if(!errors.isEmpty()) { // 에러가 발생한 경우
			return FORM_VIEW;
		}
		
		// 에러가 발생하지 않은 경우 회원가입 진행
		try {
			joinService.join(joinReq);
			return "/WEB-INF/view/joinSuccess.jsp";
		} catch(DuplicatedIdException e) {
			errors.put("duplicated", Boolean.TRUE);
			return FORM_VIEW;
		}
	}

}
