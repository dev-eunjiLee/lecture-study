package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.dao.ArticleDao;
import article.model.Article;
import article.service.DeleteRequest;
import article.service.DeleteService;
import auth.service.User;
import mvc.command.CommandHandler;

public class DeleteArticleHandler implements CommandHandler {
	
	/*
	 * delete로 넘어오는 방법
	 * 1. 직접 URL 입력 > GET 방식 
	 * 2. readArticle에서 넘어옴 > GET 방식
	 * 
	 * session의 authUser값 확인해서 있는 경우만 삭제 필요
	 * authUser값이 있어도 id값이 다르면 걸러야 한다.
	 */
	
	private static final String DELETE_FAIL_VIEW = "/WEB-INF/view/deleteFail.jsp";
	private static final String DELETE_SUCCESS_VIEW = "/WEB-INF/view/deleteSuccess.jsp";
	
	private DeleteService deleteService = new DeleteService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		// POST인 경우 발생 X > 내가 구현해놓지 않았기 때문에 만약 발생한다면 에러처리 필요
		if(req.getMethod().equalsIgnoreCase("GET")) {
			return processDelete(req, res);
		} else {
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	private String processDelete(HttpServletRequest req, HttpServletResponse res) {
		
		User user = (User) req.getSession().getAttribute("authUser");
		
		String no = req.getParameter("no"); // 삭제할 게시글 번호
		int articleNumber = Integer.parseInt(no);
		
		String userId = user.getId(); // 삭제를 요청한 사용자의 id		
		
		DeleteRequest delReq = new DeleteRequest(articleNumber, userId);
		
		req.setAttribute("delReq", delReq);
		
		/*
		 * deleteResult는 article 테이블에서 1번,
		 * content 테이블에서 1번 총 2번 쿼리문이 실행되었기 때문에 2가 정상값이다.
		 */
		
		int deleteResult = deleteService.delete(delReq);
		
		if(deleteResult > 1) {
			return DELETE_SUCCESS_VIEW;
		} else {
			return DELETE_FAIL_VIEW;
		}
	}
}
