package article.service;

import java.sql.Connection;
import java.sql.SQLException;

import article.dao.ArticleContentDao;
import article.dao.ArticleDao;
import article.model.Article;
import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ModifyArticleService {

	private ArticleDao articleDao = new ArticleDao();
	private ArticleContentDao contentDao = new ArticleContentDao();
	
	public void modify(ModifyRequest modReq) {
		Connection conn = null;
		try {
			conn = ConnectionProvider.getConnection();
			conn.setAutoCommit(false); // 트랜잭션은 시작하지만, 커밋은 따로 코드 입력해야 실행 가능
			
			/*
			 *  Article - 게시글 정보 보관용 DTO
			 *  articleDao.selectById(커넥션, 특정 번호): 특정 번호에 해당하는 게시글 데이터 읽기
			 *  modReq - 게시글 수정 요청에 대한 DTO
			 *  modReq.getArticleNumber() - 게시글 정보 수정을 요청한 특정 번호값 return
			 */
		
			Article article = articleDao.selectById(conn, modReq.getArticleNumber());
		
			if(article == null) {
				/*
				 *  article이 비어있는 경우
				 *  > modReq에 입력받은 특정 번호에 해당하는 게시글 데이터가 없는 경우
				 */
				throw new ArticleNotFoundException();
			}
			if(!canModify(modReq.getUserId(), article)) {
				/*
				 *  게시글 수정 요청자의 ID와 게시글 작성자의 ID 비교 후 같은 경우 true 리턴
				 *  조건 앞에 !가 붙었기 때문에 아래 코드가 실행되는 경우는 시글 수정 권한이 없는 경우
				 */
				throw new PermissionDeniedException(); 
			}
			/*
			 * 각각 dao 객체를 이용해 update 진행
			 */
			articleDao.update(conn, modReq.getArticleNumber(), modReq.getTitle());
			contentDao.update(conn, modReq.getArticleNumber(), modReq.getContent());
			conn.commit();
		} catch(SQLException e) {
			jdbcUtil.rollback(conn);
			throw new RuntimeException(e);
		} catch(PermissionDeniedException e) {
			jdbcUtil.rollback(conn);
			throw e;
		} finally {
			jdbcUtil.close(conn);
		}
	}

	/*
	 * 게시글을 수정할 수 있는지 검사 수행
	 * >> 수정하려는 사용자 ID가 게시글 사용자 ID와 동일하면 trure 리턴
	 */
	private boolean canModify(String modifyingUserId, Article article) {
		return article.getWriter().getId().equals(modifyingUserId);
	}
}