package article.service;

/*
 * 게시글의 전체(정보 + 콘텐츠)를 보기 위해서 ArticleData를 return하는 메소드
 */
import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.connection.ConnectionProvider;

public class ReadArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	public ArticleData getArticle(int articleNum, boolean increaseReadCount) {
		
		try(Connection conn = ConnectionProvider.getConnection()){
			
			/*
			 * 파라미터로 들어온 번호에 해당하는 게시글에 대한 정보 찾기
			 * ... 만약 null이면 ArticleNotFoundException 처리
			 */			
			
			Article article = articleDao.selectById(conn, articleNum);
			if(article==null) {
				throw new ArticleNotFoundException();
			}
			
			/*
			 * 파라미터로 들어온 번호에 해당하는 게시글 내용 찾기
			 * ... 만약 null이면 ArticleContentNotFoundException 처리
			 */	
			ArticleContent content = contentDao.selectById(conn, articleNum);
			if(content==null) {
				throw new ArticleContentNotFoundException();
			}
			
			/*
			 * increaseReadCount가 true면,
			 * 파라미터로 들어온 articleNum으 조회수를 1 증가시킨다.
			 * (increaseReadCount는 조회수 증가 여부므를 결정)
			 * 
			 * ???... 만약에 read_cnt를 여기서 증가시켰어 근데 이게 저 return에는 반영 안되지 않나...?
			 *	>> 답변은 아래 주석 참조
			 */
			if(increaseReadCount) {
				articleDao.increaseReadCount(conn, articleNum);
			}
			return new ArticleData(article, content); // list창에서만 read_cnt가 필요하기 때문에 read단게에서는 굳이 새로 업데이트 할 필요 없이 기존 article을 가져온다.
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
