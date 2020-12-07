<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 수정</title>
</head>
<body>

<form action="modify.do" method="post">
<input type="hidden" name="no" value="${modReq.articleNumber}">
	<p>
		번호:<br>
		${modReq.articleNumber}
	</p>
	<p>
		제목: <br>
		<input type="text" name="title" value="${modReq.title}">
	</p>
	<p>
		내용:<br>
		<textarea name="content" rows="5" cols="30">${modReq.content}</textarea>
	</p>
	<input type="submit" value="글 수정">
</form>

</body>
</html>