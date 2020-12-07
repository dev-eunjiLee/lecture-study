<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>암호 변경</title>
</head>
<body>

<form action="changePwd.do" method="post">
	<p>
		현재 암호: <br>
		<input type="password" name="curPwd">
		<c:if test="${errors.curPwd}">
		<!-- ChangePasswordHandler에서
			curPwd가 비어있는 경우 True가 나오도록 errors를 설정해두었다. -->
			현재 암호를 입력하세요.
		</c:if>
		<c:if test="${!errors.badCurPwd}">
		<!-- ChangePasswordService에서 설정된
			기존 비밀번호가 제대로 입력되지 않은 경우 던져진 에러를 처리한다. -->
			현재 암호가 일치하지 않습니다.
		</c:if>
	</p>
	<p>
		새 암호:<br>
		<input type="password" name="newPwd">
		<c:if test="${errors.newPwd}">
		<!-- 변경될 새 비밀번호를 입력하지 않은 경우 -->
			새 암호를 입력하세요.
		</c:if>
	</p>
	<input type="submit" value="암호변경">

</form>

</body>
</html>