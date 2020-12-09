package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.me") // .me로 끝나는 모든 값을 catch한다.
public class MemberFrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// doGet, doPost에서 호출해서 사용하는 실질적인 처리 메소드
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) {
		
		// 미완성
			
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8"); // post 방식인 경우 req를 사용할 때 인코딩 선작업 필수
		doProcess(request, response);
	}

}
