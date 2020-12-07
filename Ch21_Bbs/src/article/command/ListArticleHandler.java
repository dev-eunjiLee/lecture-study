package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticlePage;
import article.service.ListArticleService;
import mvc.command.CommandHandler;

public class ListArticleHandler implements CommandHandler{

	/*
	 * ListArticleService
	 * 페이지 번호를 파라미터로 넣어서 해당 페이지에 출력되어야 할 게시글 데이터와 페이징 정보를 담고 있는 ArticlePage 객체 리턴
	 */
	private ListArticleService listService = new ListArticleService();
	
	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String pageNoVal = req.getParameter("pageNo");
		int pageNo = 1;
		
		/*
		 * 파라미터를 통해 입력받은 페이지번호가 있을 경우 해당 페이지 번호를 이용해 articlePage 셋팅
		 */
		
		if(pageNoVal != null) {
			pageNo = Integer.parseInt(pageNoVal);
		}
		ArticlePage articlePage = listService.getArticlePage(pageNo);
		req.setAttribute("articlePage", articlePage); // 이 값들은 ControllerUsingURI의 forward를 통해 listArticle.jsp로 날라간다.
		return "/WEB-INF/view/listArticle.jsp";
	}

}
