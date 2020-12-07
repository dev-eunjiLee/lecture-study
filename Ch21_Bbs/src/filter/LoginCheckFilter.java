package filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * 보통 웹 사이트에서 회원 정보를 수정하려면 먼저 로그인해야 한다.
 * 로그인하지 않은 상태에서 회원 정보 수정 URL에 접근하면 로그인 폼으로 이동해서 먼저 로그인하도록 유도해야 한다.
 * 회원 정보 수정 기능처럼 로그인 한 경우에만 접근할 수 있는 기능을 구현하려면, 다음과 같은 코드를 추가해야 한다.
 * 
 * - 로그인 여부 검사
 * - 로그인하지 않았으면 로그인 화면으로 이동
 * - 로그인했다면 요청한 기능 실행
 * 
 * Filter를 구현한 LoginCheckFilter를 이용해 전체 과정에서 해당 기능 사용 가능
 * 
 */

public class LoginCheckFilter implements Filter {

	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		
		/*
		 * HttpServletRequest는 ServletRequest를 구현
		 * HttpServletResponse는 ServletResponse를 구현
		 * 
		 * >> HttpServletRequest는 ServletRequest의 자식 클래스
		 * 인터페이스 변수(ServletRequest request)는 본인을 구현한 클래스의 인스턴스 변수(HttpServletRequest httpRequest)로 참조될 수 있다.
		 * (단, 강제 형변환 필수)
		 */
		
		HttpServletRequest request = (HttpServletRequest) req;
		HttpSession session = request.getSession();
		
		/*
		 * session의 값이 null이거나 session의 authUser값이 null인 경우 > 로그인 정보가 없는 경우 > 로그인 창으로 이동
		 * 로그인 정보가 있는 경우 다음 필터로 넘어가서 수행
		 */
		
		if(session==null||session.getAttribute("authUser")==null) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.sendRedirect(request.getContextPath()+"/login.do"); // 로그인 창 이동
		} else {
			chain.doFilter(request, res);
		}
		
	}
	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	public void destroy() {
	
	}

	

	

}
