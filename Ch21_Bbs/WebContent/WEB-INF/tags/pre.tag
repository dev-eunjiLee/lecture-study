<%@ tag body-content="empty" pageEncoding="utf-8"%>											
<%@ tag trimDirectiveWhitespaces="true" %>													
<%@ attribute name = "value" type="java.lang.String" required="true" %>													<!-- value라는 속성을 만들고 이 속성 값은 필수로 입력 받아야 하며 타입은 String -->
<%
	
	value = value.replace("&", "&amp"); // 
	value = value.replace("<", "&lt"); // 
	value = value.replace(" ", "&nbsp"); // 
	value = value.replace("\n", "\n<br>"); 
	
%>
<%= value%>
<!-- 최대한 폼에 입력한 내용을 그대로 보여주기 위해 치환 작업을 해주는 커스텀 태그 -->
<!-- empty: 몸체가 없이 사용 -->
<!-- trimDirectiveWhitespaces: 공백 제거 -->
