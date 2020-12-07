package article.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.service.ArticleContentNotFoundException;
import article.service.ArticleData;
import article.service.ArticleNotFoundException;
import article.service.ReadArticleService;
import mvc.command.CommandHandler;

public class ReadArticleHandler implements CommandHandler{
	
	private ReadArticleService readService = new ReadArticleService();

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		/*
		 * 파라미터로 no(페이지번호)받아서 noVal에 저장
		 * >> articleNum으로 변경해서 저장(int 타입)
		 */
		String noVal = req.getParameter("no");
		int articleNum = Integer.parseInt(noVal);
		
		System.out.println("여기는 되냐요?");
		
		try {
			
			/*
			 * getArticle(페이지 번호, 조회수 증가 여부 결정 변수) >> articleData 리턴
			 */
			
			ArticleData articleData = readService.getArticle(articleNum,true);
			req.setAttribute("articleData", articleData);
			return "/WEB-INF/view/readArticle.jsp";
		} catch(ArticleNotFoundException | ArticleContentNotFoundException e) {
			
			/*
			 * getServletContext(): ServletContext 출력
			 * 
			 * ServletContext란
			 *  - 웹 애플리케이션이 실행되면서 애플리케이션 전체의 공통 자원이나정보를 미리 바인딩해서 서블릿들이 공유해서 사용
			 *  - JSP의 application 기본객체
			 *  
			 * log()메소드
			 *  - a log file에 기록
			 *  - log(java.lang.String message, java.lang.Throwable throwable)
			 *		Writes an explanatory message and a stack trace for a given Throwable exception to the servlet log file.
			 *		-> 던져진 예외를 위해서 설명이 되는 메세지와 stack trace를 servlet log 파일에 출력한다. 
			 *  
			 * 로그란
			 *  - 애플리케이션의 상태를 관찰할 수 있도록 애플리케이션이 제공하는 정보
			 */
			
			/*
			 * 콘솔 출력 위치 확인할 것: https://devday.tistory.com/m/2338?category=501907
			 */
			
			req.getServletContext().log("no article", e);
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
	}
}
