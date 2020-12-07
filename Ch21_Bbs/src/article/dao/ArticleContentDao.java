package article.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import article.model.ArticleContent;
import jdbc.jdbcUtil;

/*
 * 게시글 번호, 게시글 내용을 오라클 데이터베이스에 입력
 */

public class ArticleContentDao {
	
	public ArticleContent insert(Connection conn, ArticleContent content) throws SQLException {
		
		PreparedStatement pstmt = null;
		
		try {
			
			/*
			 * ArticleContent객체에 담긴 no, content 내용을 오라클 데이터베이스에 insert
			 * 삽입한 행 수 만큼을 insertedCount로 저장
			 */
			
			pstmt = conn.prepareStatement(
					"insert into article_content (article_no, content) values (?,?)");
			pstmt.setLong(1, content.getNumber());
			pstmt.setString(2, content.getContent());
			int insertedCount = pstmt.executeUpdate();
			
			if(insertedCount > 0) {
				return content; // 입력받은 파라미터를 리턴
			} else {
				return null;
			}
			
		} finally {
			jdbcUtil.close(pstmt);
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 조회 기능 */
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/*
	 * 게시글 번호에 맞는 content를 오라클 데이터베이스에서 가져와 ArticleContent 타입의 content 객체를 생성한 후 해당 값을 리턴
	 */
	public ArticleContent selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement("select * from article_content where article_no = ?");
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			
			ArticleContent content = null;
			
			if(rs.next()) {
				content = new ArticleContent(rs.getInt("article_no"), rs.getString("content"));
			}
			return content;
		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(pstmt);
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 수정 기능 */
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	
	/* content 칼럼의 값 변경하는 메소드*/
	public int update(Connection conn, int no, String content) throws SQLException {
		
		try(PreparedStatement pstmt
				= conn.prepareStatement("update article_content set content = ? where article_no = ?")){
			pstmt.setString(1, content);
			pstmt.setInt(2, no);
			pstmt.executeUpdate();
		}
		
		return 0;
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 삭제 기능 */
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	// 게시글 콘텐츠 삭제
	public int delete(Connection conn, int no) throws SQLException {
		
		try(PreparedStatement pstmt = conn.prepareStatement("delete from article_content where article_no = ?")){
			pstmt.setInt(1, no);
			int delete = pstmt.executeUpdate();
			return delete;
		} 
		
	}
}
