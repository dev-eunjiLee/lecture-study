<%@ tag import="javax.servlet.http.HttpSession" %>
<%@ tag language="java" pageEncoding="UTF-8" body-content="scriptless" %>
<%@ tag trimDirectiveWhitespaces="true"%>
<!-- body-content: 몸체 내용의 종류를 입력-->
	<!-- scriptless: EL 및 태그가 처리된 몸체 내용 사용 -->
	<!-- tagdependent: 몸체 내용 자체를 데이터로 사용  -->
<!-- trimDirectiveWhitespaces: 출력 결과에서 템플릿 텍스트의 공백 문자를 제거할지 여부 결정 -->
<%
	HttpSession httpSession = request.getSession(false);
	if(httpSession==null || httpSession.getAttribute("authUser")==null){
		// session이 null이고 session에서 authUser값을 가져왔을 때 null이면 몸체 내용을 수행하겠다.
%>
	<jsp:doBody />
<%
	} 
%>	