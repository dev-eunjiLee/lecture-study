<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 게시판 예제</title>
</head>
<body>

jstl 사용한 경우<br>
<c:if test="${!empty authUser}">
<!-- authUser가 비어있지 않은 경우 해당 코드 수행 -->
<!-- authUser: LoginHandler에서 로그인이 성공적으로 수행된 경우 id, name을 갖고 있는 User타입의 객체 -->
	${authUser.name}님 안녕하세요.
	<a href="logout.do">[로그아웃하기]</a>
	<a href="changePwd.do">[암호변경하기]</a>
</c:if>
<c:if test="${empty authUser}">
<!-- authUser가 비어있는 경우 -->
	<a href="join.do">[회원가입하기]</a>
	<a href="login.do">[로그인하기]</a>
</c:if>

<%--  
<hr>

커스텀 태그 사용한 경우<br>

<u:isLogin>
	CT:${authUser.name}님 안녕하세요.
	<a href="logout.do">[로그아웃하기]</a>
	<a href="changePwd.do">[암호변경하기]</a>
</u:isLogin>
<u:notLogin>
	<a href="join.do">[회원가입하기]</a>
	<a href="login.do">[로그인하기]</a>
</u:notLogin>
<!-- 여기에서 a태그의 하이퍼링크로 넘어가는 것은 get방식이다. -->
--%>
</body>
</html>