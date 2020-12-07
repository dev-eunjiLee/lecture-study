package member.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import auth.service.User;
import member.service.ChangePasswordService;
import member.service.InvalidPasswordException;
import member.service.MemberNotFoundException;
import mvc.command.CommandHandler;

public class ChangePasswordHandler implements CommandHandler{
	
	private static final String FORM_VIEW = "/WEB-INF/view/changePwdForm.jsp";
	private ChangePasswordService changePwdSvc = new ChangePasswordService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if(req.getMethod().equalsIgnoreCase("POSt")) {
			return processSubmit(req, res);
		} else {
			// method 방식이 get, post 모두 아닌 경우 에러 출력
			res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
			return null;
		}
	}
	
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// 원래 User타입으로 만들어졌던 객체기 때문에 User 형변환해서 사용 가능
		User user = (User) req.getSession().getAttribute("authUser");
		
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		/*
		 * 동일한 이름으로 Session에 저장하게 되면, 항상 덮어씌우게 된다.
		 */
		
		String curPwd = req.getParameter("curPwd"); // form에서 넘어오는 값
		String newPwd = req.getParameter("newPwd"); 
		
		if(curPwd == null || curPwd.isEmpty()) {
			// 현재 비밀번호를 입력하지 않은 경우
			errors.put("curPwd",Boolean.TRUE);
		}
		if(newPwd == null || newPwd.isEmpty()) {
			// 변경될 비밀번호를 입력하지 않은 경우
			errors.put("newPwd",Boolean.TRUE);
		}
		if(!errors.isEmpty()) {
			// 에러가 있는 경우 원래 FORM으로 돌아간다.
			return FORM_VIEW;
		}
		
		/*
		 * 에러가 발생하지 않은 경우 > 비밀번호 변경 실행 후 변경 성공 페이지로 이동
		 */
		
		try {
			changePwdSvc.changePassword(user.getId(), curPwd, newPwd);
			return "/WEB-INF/view/changePwdSuccess.jsp";
		} catch (InvalidPasswordException e) {
			errors.put("badCurPwd", Boolean.TRUE);
			return FORM_VIEW;
		} catch (MemberNotFoundException e) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		} 
		
	}

}
