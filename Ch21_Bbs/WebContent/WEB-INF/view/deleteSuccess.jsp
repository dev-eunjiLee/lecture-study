<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 삭제 성공</title>
</head>
<body>
	${authUser.name}님이 작성한 번호-${delReq.articleNumber}의 게시글이 무사히 삭제되었습니다.
	<a href="list.do">게시글 목록 다시보기</a>
</body>
</html>