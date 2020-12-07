package article.model;

/*
 * 게시글 작성자 정보를 담는 DTO
 */

public class Writer {
	
	private String id;
	private String name;
	
	public Writer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
