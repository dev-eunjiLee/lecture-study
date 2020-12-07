package article.service;

import java.util.List;

import article.model.Article;

/*
 * DTO
 * 
 * 게시글 목록 구현을 위해 게시글 데이터와 페이징 관련 정보를 담을 ArticlePage 클래스
 * - 한 화면에 페이지 번호는 총 5개씩 출력
 * - 한 페이지 번호에 포함될 게시글의 양은 size 참조
 */

public class ArticlePage {

	private int total; // 전체 개시글 개수
	private int currentPage; // 사용자가 요청한 페이지 번호
	private List<Article> content; // 화면에 출력할 게시글 목록 보관
	private int totalPages; // 전체 페이지 개수
	private int startPage; // 화면 하단에 보여줄 페이지 이동 링크의 시작 번호
	private int endPage; // 화면 하단에 보여줄 페이지 이동 링크의 끝 번호
	
	/*
	 * int size:: 한 페이지에서 보여줄 게시글의 양
	 */
	
	/*
	 * 파라미터 생성자 내부에서 수식을 활용해 totalpages, startPage, endPage 구하기
	 * 그 외 값은 파라미터로 들어와야 한다.
	 */
	
	public ArticlePage(int total, int currentPage, int size, List<Article> content) {
		this.total = total;
		this.currentPage = currentPage;
		this.content = content;
		
		if(total == 0) { // 전체 게시글의 개수가 0
			totalPages = 0;
			startPage = 0;
			endPage = 0;
		} else {// 게시글이 1개라도 있는 경우
			totalPages = total/size;
			if(total % size > 0) {
				totalPages++; // size로 나누고 나서 나머지가 발생하면 해당 페이지를 표현하기 위해 totalPage를 +1 해준다.
			}
			/*
			 * modVal: 현재 페이지를 5로 나눈 나머지
			 * >> 이 값이 필요한 이유는?
			 * 
			 * 아래 주석 참고
			 */
			int modVal = currentPage % 5;// 현재 페이지를 5로 나눈 후 나머지
			startPage = currentPage/5*5+1;
			
			/*
			 * 예시
			 * currentPage:4
			 * startPage = 4/5*5+1 = 0*5+1 = 1
			 * 
			 * currentPage: 9
			 * startpage = 9/5*5+1 = 1*5+1 = 6
			 * 
			 * ... 5의 배수에 +1 한 값으로만 startPage가 구성된다.
			 * 
			 * 예외
			 * curretnPage: 15
			 * startPage = 15/5*5+1 = 3*5+1 = 16
			 * ... 현재 페이지가 5의 배수인 경우(modVal==0) -5를 해주어야 정상적인 startPage가 출력 
			 */
			
			if(modVal == 0) startPage -= 5;
			
			/*
			 * startPage 값을 기준으로 endPage값 구하기
			 *  - 디폴트: stargPage + 4;
			 *  - ... 예외 에시
			 *  	        현재 페이지가 16이고, 전체 페이지 수가 18인 경우 endPage는 원래 20이어야 맞지만
			 *  		전체 페이지 수보다 클 수 없으니까 endPage로 맞춰준다.
			 */
			
			endPage = startPage + 4; // 마지막 페이지 번호
			if(endPage > totalPages) endPage = totalPages;
		}
	}

	/*
	 * 그 외 메소드
	 * 1. hasNoArticles()
	 * 	- 가지고 있는 게시글이 있는지 없는지 판단!
	 *  - 게시글이 있으면 false, 게시글이 없으면 true 리턴
	 *  
	 * 2. hasArticles()
	 *  - 가지고 있는 게시글이 1개라도 있으면 true리턴, 없으면 false
	 */
	
	public boolean hasNoArticles() {
		return total == 0;
	}
	
	public boolean hasArticles() {
		return total > 0;
	}
	
	/*
	 * getter
	 */
	public int getTotal() {
		return total;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<Article> getContent() {
		return content;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
}
