<%@tag import="java.util.Date"%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8"%>
<%@ tag trimDirectiveWhitespaces="true"%>
<%
//dddd
	HttpSession httpSession = request.getSession(false);
	if(httpSession != null && httpSession.getAttribute("authUser") != null) {
	// session이 null이 아니고 session에서 authUser값을 가져왔을 때 null이 아니면 몸체 내용을 수행하겠다.
%>
<jsp:doBody />
<%
	}
%>

<!-- body-content: 몸체 내용의 종류를 입력-->
	<!-- scriptless: EL 및 태그가 처리된 몸체 내용 사용 -->
	<!-- tagdependent: 몸체 내용 자체를 데이터로 사용  -->
<!-- trimDirectiveWhitespaces: 출력 결과에서 템플릿 텍스트의 공백 문자를 제거할지 여부 결정 -->
