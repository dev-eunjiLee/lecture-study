package article.command;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.model.Writer;
import article.service.WriteArticleService;
import article.service.WriteRequest;
import auth.service.User;
import mvc.command.CommandHandler;

public class WriteArticleHandler implements CommandHandler {

	private static final String FORM_VIEW = "/WEB-INF/view/newArticleForm.jsp";
	private WriteArticleService wrtieService = new WriteArticleService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		/*
		 *	Method방식에 따라 서로 다른 코드 진행 
		 *	>> 어떤 방식도 아닌 경우 에러 처리
		 */
		
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processForm(req, res);
		} else if(req.getMethod().equalsIgnoreCase("POST")) {
			return processSubmit(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	/*
	 * GET방식으로 들어온 경우 newArticleForm으로 넘긴다.
	 */
	private String processForm(HttpServletRequest req, HttpServletResponse res) {
		return FORM_VIEW;
	}
	
	/*
	 * POST방식으로 들어온 경우 WriteArticleService 이용해 게시글 쓰기 수행
	 */
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) {
		Map<String, Boolean> errors = new HashMap<>();
		req.setAttribute("errors", errors);
		
		/*
		 * setAttribute할 때 뒤 errors는 주소값을 가지고 있기 때문에 그 하단에 내용을 저장해도
		 * 뒤에 저장한 내용들이 req의 속성으로 들어간다. 
		 */
		
		/*
		 * Session에서 authUser(로그인 정보 가지고있는 세션) 가져와서 User에 넣어두기 
		 */
		User user = (User) req.getSession(false).getAttribute("authUser");
		
		WriteRequest writeReq = createWriteRequest(user, req);
		
		/*
		 * writeReq의 title이 비어있거나 없는경우 Map 타입의 errors에
		 * 해당 상황에 대한정보를 넣는다.
		 */
		
		writeReq.validate(errors);
		
		/*
		 * errors가 비어있지 않다는 것은 에러가 1번이라도 발생했다는 이야기이므로 다시 입력 Form으로 돌린다.
		 */
		if(!errors.isEmpty()) {
			return FORM_VIEW;
		}
		
		int newArticleNo = wrtieService.write(writeReq); // 글 번호 return
		req.setAttribute("newArticleNo", newArticleNo); // 속성에 새 글의 글번호 저장
		
		// 최종적으로 삽입에 성공했으니까 성공jsp로 넘긴다.
		return "/WEB-INF/view/newArticleSuccess.jsp";
		
	}
	
	private WriteRequest createWriteRequest(User user, HttpServletRequest req) {
		return new WriteRequest(new Writer(user.getId(), user.getName()),
				req.getParameter("title"), 
				req.getParameter("content"));
	}
}
