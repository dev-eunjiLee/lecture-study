package article.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import article.model.ArticleContent;
import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;

/*
 * Service
 * >> 게시글 쓰기 기능제공(처음으로 쓰는것! 수정X) >> 글을 작성한 후 글 번호를 return
 * >> 게시글 수정은 Modify로 시작하는 클래스들에서 처리할 예정
 */

public class WriteArticleService {
	
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	
	/*
	 * ArticleDao와 ArticleContentDao를 각각 이용해서
	 * 게시글의 정보 / 게시글 내용을 각각 오라클 데이터베이스 테이블에 입력한다.
	 */
	public Integer write(WriteRequest req) {
		
		Connection conn = null;
		
		try {
			
			/*
			 * 커넥션 풀에서 커넥션 가져와서 연결은 했지만 autoCommit은 false
			 * >> 트랜잭션은 시작하지만 commit은 나중에 잘 처리되고나면 한번에
			 */
			
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false);
			
			Article article = toArticle(req); // 오늘날짜 넣어서 Article 객체 생성
			Article savedArticle = articleDao.insert(conn, article); // 바로 윗 코드에서 만들어진 내용을 오라클데이터베이스에 insert
			if(savedArticle==null) {
				/*
				 * 오라클 데이터베이스에 삽입된 내용이 없을 경우 Exception 발생
				 */
				throw new RuntimeException("fail to insert article"); 
			}
			
			/*
			 * req.getContent >> WriteRequest에 입력되어 있는 number, content를 이용해서 ArticleContent 객체 만들기
			 * 위에서 articleDao를 거치면서 sequence를 통해 문서번호가 기입된다.
			 */
			ArticleContent content =
					new ArticleContent(savedArticle.getNumber(), req.getContent());
			ArticleContent savedContent = contentDao.insert(conn, content);
			if(savedContent == null) { // 오라클 데이터베이스의 article_content 테이블에 입력된 값이 없는 경우
				throw new RuntimeException("fail to insert article_content");				
			}
			
			conn.commit();
			
			return savedArticle.getNumber();
							
		} catch(SQLException e) {
			jdbcUtil.rollback(conn);
			throw new RuntimeException(e);				
		} catch(RuntimeException e) {
			jdbcUtil.rollback(conn);
			throw e;
		} finally {
			jdbcUtil.close(conn);
		}
	}
	
	/*
	 * 오늘 날짜로 작성 날짜, 수정날짜 적용된 Article 타입 객체 리턴
	 * > 문서번호는 오라클데이터베이스에 들어가봐야(dao)알기 때문에 현재는 null로 해서 Article객체 생성
	 */
	
	private Article toArticle(WriteRequest req) {
		Date now = new Date();
		return new Article(null, req.getWriter(), req.getTitle(), now, now, 0);
	}
	
	
	

}
