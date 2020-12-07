package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;

/*삭제 메소드*/

public class DeleteService {
	
	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	public int delete(DeleteRequest delReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션은 시작하지만 ,커밋은 따로 진행
			
			// 특정 번호에 해당하는 게시글 데이터 읽기
			Article article = articleDao.selectById(conn, delReq.getArticleNumber());
			
			if(article == null) { // 해당 번호의 게시글이 없는 경우
				throw new ArticleNotFoundException();
			}
			
			// 해당 번호의 게시글은 있지만, 아이디가 일치하지 않는 경우
			if(!canDelete(delReq.getUserId(), article)) {
				throw new PermissionDeniedException();
			}
			
			// 해당 번호의 게시글도 있고, 아이디도 일치하는 경우 >> delete 진행
			int deleteArticle = articleDao.delete(conn, delReq.getArticleNumber());
			int deleteContent =  contentDao.delete(conn, delReq.getArticleNumber());
			
			int result = deleteArticle + deleteContent;
			
			conn.commit();
			
			return result;
			
		} catch(SQLException e) {
			jdbcUtil.rollback(conn);			
			throw new RuntimeException(e);
		} catch(PermissionDeniedException e){
			jdbcUtil.rollback(conn);
			throw e;
		} finally {
			jdbcUtil.close(conn);
		}
	}
	
	
	// 삭제하려는 게시글 작성자 id와 현재 로그인된 id를 비교해서 같으면 true 리턴
	private boolean canDelete(String deleteUsingId, Article article) {
		return article.getWriter().getId().equals(deleteUsingId);
	}

}
