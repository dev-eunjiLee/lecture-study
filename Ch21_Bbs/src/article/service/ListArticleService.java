package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import article.dao.ArticleDao;
import article.model.Article;
import jdbc.connection.ConnectionProvider;
import article.dao.ArticleDao;

/*
 * 페이지 번호를 파라미터로 넣어서 해당 페이지에 출력되어야 할 게시글 데이터와 페이징 정보를 담고 있는 ArticlePage 객체 리턴
 */

public class ListArticleService {

	private ArticleDao articleDao = new ArticleDao();
	private int size = 10;
	
	public ArticlePage getArticlePage(int pageNum){
		
		try(Connection conn = ConnectionProvider.getConnection()){
		
			/*
			 * !!!교재와 다르게 내가 수정해서 값 넣었다.
			 * conn, (pageNum-1)*size+1, size*pageNum
			 * 
			 * 전체 게시글 개수 구한 후(articleDao.selectCount)
			 * 지정한 범위의 게시글 읽어오기(articleDao.select(conn, startRow, total))
			 * 	- startRow: 파라미터로 들어온 페이지 번호 - 1 * size ... size는 한 페이지에 보여줄 게시글의 개수
			 *  - total: 교재에 total로 명시되어 있지만 내가 볼 때는 endRow가 더 좋은 듯 하다. 해당 페이지의 마지막에 게시글 번호 지칭하는 파라미터
			 *  
			 * ArticlePage(전체 게시글 개수, 현재 페이지 번호, 한 페이지에 보여줄 게시글 양, 게시글 객체들) 
			 * 
			 */
			
			int total = articleDao.selectCount(conn);
			List<Article> content = articleDao.select(conn, (pageNum-1)*size+1, size*pageNum);
			
			return new ArticlePage(total, pageNum, size, content);
			
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
