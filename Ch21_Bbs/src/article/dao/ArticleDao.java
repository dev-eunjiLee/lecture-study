package article.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import article.model.Article;
import article.model.Writer;
import jdbc.jdbcUtil;
import jdbc.connection.ConnectionProvider;

public class ArticleDao {
	
	/*
	 * 새 게시글 오라클 데이터베이스에 추가하기
	 */

	public Article insert(Connection conn, Article article) throws SQLException {
		
		PreparedStatement pstmt = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			
			/*
			 * Article 타입의 artilce 객체에서 값을 받아 오라클데이터에 입력
			 * 입력이 되면 1(입력한 수)을 insertCount에 저장
			 */
			pstmt = conn.prepareStatement(
					"insert into article (article_no, writer_id, WRITER_NAME, title, regdate, moddate, read_cnt)"
					+ "values(article_no_seq.NEXTVAL,?,?,?,?,?,0)");
			pstmt.setString(1, article.getWriter().getId());
			pstmt.setString(2, article.getWriter().getName());
			pstmt.setString(3, article.getTitle());
			pstmt.setTimestamp(4, toTimeStamp(article.getRegDate()));
			pstmt.setTimestamp(5, toTimeStamp(article.getModifiedDate()));
			int insertCount = pstmt.executeUpdate();
			
			if(insertCount > 0) {
				stmt = conn.createStatement();
				
				/*
				 * article_no가 가장  큰 값(가장 최근에 입력한 값)을 ResulstSet에 넣는다.
				 */
				
				/*
				 * 시퀀스 번호를 알기 위해서 max(article_no)를 이용한다.
				 * rs.getInt(1)을 하면 데이터의 가장 첫번째 값인 article_no가 나온다.
				 * 해당 article_no를 이용해서 new Article 객체 생성
				 */
				
				rs = stmt.executeQuery("select max(article_no) from article");
				if(rs.next()) {					
					
					Integer newNum = rs.getInt(1);
					return new Article(newNum, article.getWriter(), article.getTitle(), article.getRegDate(), article.getModifiedDate(), 0);
				}
			}
			/*
			 * 코드가 제대로 실행이 되지 않은 경우 null값을 리턴
			 */
			return null;
		
		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(stmt);
			jdbcUtil.close(stmt);
		}
		
	}
	
	/*
	 * Date.getTime()
	 * 현재 시간을 long 타입으로 return
	 * 
	 * toTimeStamp(date)
	 * > 현재 날짜를 TimeStamp타입으로 리턴
	 */
	private Timestamp toTimeStamp(Date date) {
		return new Timestamp(date.getTime());
	}
	
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 목록 조회 관련 기능*/
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	
	/*
	 * 전체 게시글 개수 구하기
	 */
	
	public int selectCount(Connection conn) throws SQLException {
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs=stmt.executeQuery("select count(*) from article"); // 전체 행 개수 세기
			
			if(rs.next()) {
				return rs.getInt(1); // 전체 행 개수를 int로 리턴
			}
			return 0; // 선택된 행이 없는 경우
			
		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(stmt);
		}
	}
	
	
	/*
	 * 지정한 범위의 게시글 읽어오기
	 */
	
	public List<Article> select(Connection conn, int startRow, int size) throws SQLException{
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			
			/*
			 * 글 번호로 내림차순하여 article 테이블 데이터 정렬
			 */
			
			String sql = "select * from article order by article_no desc";
			/*
			 * 글 번호로 내림차순된 article데이터에서 열번호 + 전체 행을 선택
			 */
			
			sql = "select rownum rnum, article_no, writer_id, writer_name, title, regdate, moddate, read_cnt from ("+sql+")";
			
			/*
			 * 글 번호로 내림차순된 article 데이터에서 열번호+전체행을 선택한 후 열번호를 기준으로 ?번부터 ?번까지만 선택
			 */
			sql = "select * from ("+sql+") where rnum between ? and ?";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, size);
			
			rs = pstmt.executeQuery();
			
			List<Article> result = new ArrayList<>();
			while(rs.next()) {
				result.add(convertArticle(rs)); // 생성된 article 객체를 result에 삽입
			}
			return result;
			
			
		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(pstmt);
		}
		
		
	}
	
	/*
	 * 오라클 데이터 처리 결과가 담긴 rs를 이용해 article 객체를 생성
	 */
	
	private Article convertArticle(ResultSet rs) throws SQLException {
		return new Article(rs.getInt("article_no"),
							new Writer(rs.getString("writer_id"), 
										rs.getString("writer_name")),
							rs.getString("title"),
							rs.getTimestamp("regdate"),
							rs.getTimestamp("moddate"),
							rs.getInt("read_cnt"));
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 조회 기능 */
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/*
	 * 조회 관련 기능
	 * 1. 특정 번호에 해당하는 게시글 데이터 읽기
	 */
	
	public Article selectById(Connection conn, int no) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			pstmt = conn.prepareStatement(
					"select * from article where article_no = ?");
			pstmt.setInt(1, no);
			
			rs = pstmt.executeQuery();
			Article article = null;
			if(rs.next()) {
				article = convertArticle(rs); // rs에 담긴 내용을 이용해 Article 타입의 article 객체 생성
			}
			return article;
		} finally {
			jdbcUtil.close(rs);
			jdbcUtil.close(pstmt);
		}
	}
	
	/*
	 * 조회 관련 기능
	 * 2. 특정 번호에 해당하는 게시글 데이터의 조회수 증가하기
	 */
	
	public void increaseReadCount(Connection conn, int no) throws SQLException {
		try(PreparedStatement pstmt = conn.prepareStatement("update article set read_cnt = read_cnt + 1"
															+ "where article_no = ?")){
			pstmt.setInt(1, no);
			pstmt.executeUpdate();
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 수정 기능 (교재: 664)*/
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/*
	 * 게시글 데이터 수정 기능
	 * - 파라미터로 입력받은 게시글 번호와 제목을 이용해서 데이터 수정
	 * - 수정 날짜는 현재 시간으로 기입
	 * - 데이터 업데이트가 진행된 행의 수를 return
	 * 
	 */
	
	public int update(Connection conn, int no, String title) throws SQLException {
		try(PreparedStatement pstmt
				= conn.prepareStatement("update article set title = ?, moddate = sysdate where article_no = ?")){
			pstmt.setString(1, title);
			pstmt.setInt(2, no);
			return pstmt.executeUpdate();
		}
	}
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	/* 게시글 삭제 기능 */
	
	/////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////
	
	// 게시글 콘텐츠 삭제
		public int delete(Connection conn, int no) throws SQLException {
		
			try(PreparedStatement pstmt = conn.prepareStatement("delete from article where article_no = ?")){
			pstmt.setInt(1, no);
			int delete = pstmt.executeUpdate();
			return delete;
		}
	}
		
}