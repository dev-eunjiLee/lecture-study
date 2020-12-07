package util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharacterEncodingFilter implements Filter {
	
	/*
	 * 필터의 라이프사이클: 생성 > 초기화 > 필터 > 종료
	 * 
	 * 호출 > init() > doFilter() > destroty
	 */
	
	private String encoding;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		req.setCharacterEncoding(encoding);
		chain.doFilter(req, res);
	}
	
	/*
	 * web.xml에서 입력한 초기 파라미터에 encoding값을 찾아 필터로 설정
	 * 만약 입력된 encoding 파라미터값이 없을 경우 UTF-8을 default로 사용
	 */
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		encoding = config.getInitParameter("encoding");
		if(encoding==null) {
			encoding="UTF-8";
		}
	}
	
	@Override
	public void destroy() {
	
	}

}
