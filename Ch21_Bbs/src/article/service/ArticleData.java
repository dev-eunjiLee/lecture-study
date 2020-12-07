package article.service;

import article.model.Article;
import article.model.ArticleContent;

/*
 * DTO
 * 
 * Article객체와 ArticleContent 객체를 한 객체에 담기 위한 용도로 사용
 */

public class ArticleData {

	private Article article;
	private ArticleContent content;
	
	public ArticleData(Article article, ArticleContent content) {
		this.article = article;
		this.content = content;
	}

	public Article getArticle() {
		return article;
	}

	/*
	 * ArticleContent 객체에서 content만 가져온다.
	 */
	public String getContent() {
		return content.getContent();
	}
}
