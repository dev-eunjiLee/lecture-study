<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 목록</title>
</head>
<body>

<table border="1">
	<tr>
		<td colspan="4"><a href="write.do">[게시글쓰기]</a></td>
	</tr>
	<tr>
		<td>번호</td>
		<td>제목</td>
		<td>작성자</td>
		<td>조회수</td>
	</tr>
	
<!-- ListArticleHandler에서 request의 속성으로 해당 페이지 번호에 맞는 정보를 담은 ArticlePage를 객체를 설정해둠-->
<!-- hasNoArticles():: 게시글이 0개인 경우 true 리턴 -->
<c:if test="${articlePage.hasNoArticles()}">
<!-- 해당 페이지에 해당되는 게시글이 0개인 경우 c:if의 바디 실행 -->
	<tr>
		<td colspan="4">게시글이 없습니다.</td>
	</tr>
</c:if>
<c:forEach var="article" items="${articlePage.content}">
<!-- 게시글 번호, 제목, 작성자, 조회수 출력: articlePage에서 List<Article> 객체 활용-->
	<tr>
		<td>${article.number}</td>
		<td>
			<a href="read.do?no=${article.number}&pageNo=${articlePage.currentPage}">
				<c:out value="${article.title}"/>
			</a>
		</td>
		<td>${article.writer.name}</td>
		<td>${article.readcount}</td>
	</tr>
</c:forEach>
<c:if test="${articlePage.hasArticles()}">
<!-- 가지고 있는 게시글이 1개라도 있는 경우 바디 실행 -->
	<tr>
		<td colspan="4">
		<!-- 페이지 하단에 페이지 번호 쭉 나열 -->
			<c:if test="${articlePage.startPage>5}">
			<!-- 페이지 하단에 보여줄 페이지 이동 링크의 시작번호가 5보다 큰 경우-->
			<!-- [이전]을 클릭하면 5페이지 전으로 한번에 이동 -->
				<a href="list.do?pageNo=${articlePage.startPage-5}">[이전]</a>
			</c:if>
			<c:forEach var="pNo" begin="${articlePage.startPage}" end="${articlePage.endPage}">
				<a href="list.do?pageNo=${pNo}">[${pNo}]</a>			
			</c:forEach>
			<c:if test="${articlePage.endPage<articlePage.totalPages}">
			<!-- [다음]을 클릭하면 5페이지 다음으로 한번에 이동 -->			
				<a href="list.do?pageNo=${articlePage.startPage+5}">[다음]</a>
			</c:if>
		</td>
	</tr>
</c:if>
</table>

</body>
</html>