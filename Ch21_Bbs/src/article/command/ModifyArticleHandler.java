package article.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ModifyArticleService;
import article.service.ModifyRequest;
import article.service.PermissionDeniedException;
import article.service.ReadArticleService;
import auth.service.User;
import mvc.command.CommandHandler;

/*
 * 수정할 게시글의 데이터를 보여주거나 or 게시글을 수정하거나
 * 
 * 구현 방식
 * - Get 방식: 수정할 게시글 데이터를 읽어와 폼에 보여준다.
 * - Post 방식: 전송한 요청 파라미터를 이용해서 게시글을 수정한다.
 * 			    파라미터가 값이 잘못된 경우, 전송한 데이터를 이용해서 폼을 다시 보여준다.
 */

public class ModifyArticleHandler implements CommandHandler{
	
	private static final String FORM_VIEW = "/WEB-INF/view/modifyForm.jsp";
	
	private ReadArticleService readService = new ReadArticleService(); // 게시글 읽기 기능 제공
	private ModifyArticleService modifyService = new ModifyArticleService(); // 게시글 수정 기능 제공
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		if(req.getMethod().equalsIgnoreCase("GET")) { // GET 방식
			return processForm(req,res);
		} else if(req.getMethod().equalsIgnoreCase("POST")){ // POST 방식
			return processSubmit(req, res);
		} else { // GET, POST 모두 아닌 경우 에러처리
			res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			return null;
		}
	}
	
	// GET 방식인 경우 진행될 메소드
	private String processForm(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		try {
			/*파라미터로 들어온 no값에 맞는 ArticleData 가져오기*/
			String noVal = req.getParameter("no");
			int no = Integer.parseInt(noVal);
			ArticleData articleData = readService.getArticle(no, false); // true인경우 조회수 증가> 근데 이 기능이 왜 필요?
		
			/*
			 *  로그인 아이디와 게시글 작성자 아이디 비교 후 처리
			 *   - 아이디가 다른 경우: !canModify(authUser, articleData)
			 *   - 아이디가 같은 경우: ModifyRequest객체 생성하고 해당 정보를 request에 속성으로 저장한 후 FORM_VIEW로 돌아간다.
			 */
			User authUser = (User) req.getSession().getAttribute("authUser");
			if(!canModify(authUser, articleData)) {
				res.sendError(HttpServletResponse.SC_FORBIDDEN);
				// SC_FORBIDDEN: 403, 서버가 요청은 이해했으나, 그 요청을 거부할 때 사용
				return null;
			}
			ModifyRequest modReq
			= new ModifyRequest(authUser.getId(), no, articleData.getArticle().getTitle(), articleData.getContent());
			req.setAttribute("modReq", modReq);
			return FORM_VIEW;
			
		} catch(ArticleNotFoundException e){
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
	
	/* 로그인된 id값과 게시글 작성자 id가 같은 경우 trure 리턴*/
	private boolean canModify(User authUser, ArticleData articleData) {
		String writerid = articleData.getArticle().getWriter().getId();
		return authUser.getId().equals(writerid);
	}
	
	
	// POST 방식인 경우 진행될 메소드
	private String processSubmit(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		User authUser = (User) req.getSession().getAttribute("authUser"); 
		String noVal = req.getParameter("no");
		int no = Integer.parseInt(noVal);
		
		// FORM의 POST 방식을 통해 넘어온 파라미터값을 이용해 ModifyRequest 객체 생성
		ModifyRequest modReq = new ModifyRequest(authUser.getId(), no, req.getParameter("title"), req.getParameter("content"));
		req.setAttribute("modReq", modReq);
		
		Map<String, Boolean> errors = new HashMap<>();
		
		// req에러 정보를 속성에 저장
		req.setAttribute("errors", errors);
		
		modReq.validate(errors); // title이 null이거나 비어있는 경우 에러 처리
		
		if(!errors.isEmpty()) {
			return FORM_VIEW; // errors가 비어있지 않은 경우 FORM_VIEW 리턴
		}
		
		// 에러가 비지 않은 경우
		try {
			/*
			 * 게시글이 비어있지 않고, 로그인 아이디와 게시글 아이디값이 일치하는 경우 데이터베이스의 값 수정
			 * >> 게시글 수정
			 */
			modifyService.modify(modReq);
			return "/WEB-INF/view/modifySuccess.jsp";
		} catch(ArticleNotFoundException e) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		} catch(PermissionDeniedException e) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}
	}

}
