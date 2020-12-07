package article.service;

import java.util.Map;

import article.model.Writer;

/*
 * DTO
 * - 게시글 쓰기에 필요한 데이터 제공
 */

public class WriteRequest {

	private Writer writer;
	private String title;
	private String content;
	
	public WriteRequest(Writer writer, String title, String content) {
		this.writer = writer;
		this.title = title;
		this.content = content;
	}

	public Writer getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
	
	/*
	 * 제목이 없는 경우 error 객체를 map에 추가
	 */
	public void validate(Map<String, Boolean> errors) {
		if(title==null || title.trim().isEmpty()) {
			errors.put("title",Boolean.TRUE);
		}
	}
	
	

	
}
