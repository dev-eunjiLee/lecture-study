<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 읽기</title>
</head>
<body>

<table border="1" width="100%">
	<tr>
		<td>번호</td>
		<td>${articleData.article.number}</td>
			<!-- ReadArticleHandler에서 req에 setAttribute를 통해 articleData 넣어둠 -->
	</tr>
	<tr>
		<td>작성자</td>
		<td>${articleData.article.writer.name}</td>
	</tr>
	<tr>
		<td>제목</td>
		<td>${articleData.article.title}</td>
	</tr>
	<tr>
		<td>내용</td>
		<td><u:pre value="${articleData.content}"/></td>
	</tr>
	<tr>
		<td colspan="2">
			<c:set var="pageNo" value="${empty param.pageNo?'1':param.pageNo}"/>
				<!-- param.pageNo가 비어있으면 1, 아니면 pageNo 그대로 pageNo 설정 -->
			<a href="list.do?pageNo=${pageNo}">[목록]</a>
				<!-- 파라미터로 입력받은 페이지 번호의 목록으로 돌아간다. -->
			<c:if test="${authUser.id == articleData.article.writer.id}">
				<!-- 로그인한 id와 해당 게시글 저자의 id가 일치하는 경우 수정, 삭제 기능 제공 -->
				<a href="modify.do?no=${articleData.article.number}">[게시글 수정]</a>
				<a href="delete.do?no=${articleData.article.number}">[게시글 삭제]</a>
			</c:if>
		</td>
	</tr>
</table>

</body>
</html>