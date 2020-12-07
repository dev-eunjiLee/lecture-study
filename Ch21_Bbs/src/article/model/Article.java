package article.model;

import java.util.Date;

/*
 * 게시글 정보 보관 DTO
 */
public class Article {
	private Integer number;
	private Writer writer; // Writer 타입의 클래스 정의
	private String title;
	private Date regDate;
	private Date modifiedDate;
	private int readcount;

	public Article(Integer number, Writer writer, String title, Date regDate, Date modifiedDate, int readcount) {
		this.number = number;
		this.writer = writer;
		this.title = title;
		this.regDate = regDate;
		this.modifiedDate = modifiedDate;
		this.readcount = readcount;
	}

	public Writer getWriter() {
		return writer;
	}
	
	public Integer getNumber() {
		return number;
	}

	public Writer article() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public Date getRegDate() {
		return regDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public int getReadcount() {
		return readcount;
	}

}
