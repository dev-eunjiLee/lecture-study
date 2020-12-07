package mvc.command;
import java.io.IOException;

// 531
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandHandler {
	
	/*
	 * 인터페이스
	 * 
	 * 1. 요소: 고정 상수, 추상 메소드, static 메소드, default 메소드 
	 * 2. 만드는 이유: 일관성, 강제성을 위해서
	 * 	   (여기서는 Service 기능을 하는 여러 개의 클래스를 일관성, 강제성을 이용해만들기 위해서 사용)
	 * 
	 */
	
	/*
	 * controller-서블릿
	 * model - DTO
	 * 데이터베이스 접근 - DTO
	 * service - 실제처리
	 * view-jsp
	 */
	
	/*
	 * 추상 메소드
	 * 
	 * 여기서는 웹과 관련한 정보를 처리하기 위한 추상 메소드 생성
	 */
	
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception;
	// 1. 명령어와 관련된 비즈니스 로직 처리
	// 2. 뷰 페이지에서 사용할 정보 저장
	// 3. 뷰 페이지의 URI 리턴
}
