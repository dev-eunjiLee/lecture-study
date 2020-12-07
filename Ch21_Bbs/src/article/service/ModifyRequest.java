package article.service;

import java.util.Map;

/*
 * DTO
 * - 게시글 수정 요청에 대한 정보를 가지고 있는 클래스
 */

public class ModifyRequest {
	
	/* 변수 */
	private String userId; // 게시글 수정을 요청한 유저의 ID
	private int articleNumber;
	private String title;
	private String content;
	
	/*파라미터 생성자*/
	public ModifyRequest(String userId, int articleNumber, String title, String content) {
		this.userId = userId;
		this.articleNumber = articleNumber;
		this.title = title;
		this.content = content;
	}

	/* getter 메소드 */
	public String getUserId() {
		return userId;
	}

	public int getArticleNumber() {
		return articleNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	/* 
	 * 게시글 수정 전 검사하는 메소드
	 * title이 null이거나 비어있는 경우 Map타입의 errors 변수에 에러를 나타내는 True 값 입력
	 */
	public void validate(Map<String, Boolean> errors) {
		if(title == null || title.trim().isEmpty()) {
			errors.put("title",Boolean.TRUE);
		}
	}

}
