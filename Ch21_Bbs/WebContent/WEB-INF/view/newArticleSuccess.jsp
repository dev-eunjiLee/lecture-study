<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글등록</title>
</head>
<body>

게시글을 등록했습니다.
<br>

${ctxPath = pageContext.request.contextPath}

<%--
	pageContext.request.contextPath
	>> ex> http://tskwon.tistory.com/bbs/boardList.do 인 경우: /bbs
	>> WEB-INF의 실제 JSP 경로를 가져온다.
		
	contextPath는 request의 속성이 아니기때문에 requestScope를 통해 가져올 수 없다.
	requestScope가 아닌 requst는 pageContext를 통해 사용가능
 --%>
 
<a href="${ctxPath}/article/list.do">[게시글목록보기]</a>
<a href="${ctxPath}/article/read.do?no=${newArticleNo}">[게시글내용보기]</a>
<!-- newArticleNO > WriteArticleHandler에서 가장 최근 글 번호를 저장해둔 변수 -->

</body>
</html>