package article.model;

/*
 * 작성된 article의 내용에 대한 정보를 담은 DTO
 */

public class ArticleContent {

	private Integer number;
	private String content;
	
	public ArticleContent(Integer number, String content) {
		this.number = number;
		this.content = content;
	}

	public Integer getNumber() {
		return number;
	}

	public String getContent() {
		return content;
	}
	
}
