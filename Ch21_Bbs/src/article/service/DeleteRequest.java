package article.service;

/*
 * DTO
 * - 게시글 삭제 요청에 대한 정보를 가지고 있는 클래스
 */

public class DeleteRequest {

	private int articleNumber; // 삭제 요청한 게시글 번호
	private String userId; // 삭제 요청한 사람의 id 
	
	public DeleteRequest(int articleNumber, String userId) {
		this.articleNumber = articleNumber;
		this.userId = userId;
	}

	public int getArticleNumber() {
		return articleNumber;
	}

	public String getUserId() {
		return userId;
	}
}
