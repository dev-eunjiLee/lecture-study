package mvc.controller;

import java.io.FileReader;
// 533
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.command.CommandHandler;
import mvc.command.NullHandler;

public class ControllerUsingURI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// http://localhost:8080/Ch18_MVC2/hello.dodo
	// <command, 핸들러인스턴스> 매핑 정보 저장
	private Map<String, CommandHandler> commandHandlerMap = new HashMap<>();
		
	// 1.
	// init():: 파라미터가 없는 init메소드를 사용해야 한다.
	public void init() throws ServletException {
		
		/*
		 * web.xml에서 설정한 초기화 파라미터값을 String configFile로 저장한다.
		 */
		String configFile = getInitParameter("configFile");
				
		Properties prop = new Properties(); // Map 계열
		String configFilePath = getServletContext().getRealPath(configFile); // properties까지 가는 전체 절대 경로
		
		try(FileReader fis = new FileReader(configFilePath)){ // reader를 이용해서 해당 파일의 값을 사람이 읽을 수 있는 형태로 가져온다.
			prop.load(fis); // propertis에 적혀있는 내용들을 읽어들인다.
		} catch(IOException e) {
			throw new ServletException(e);
		}
		
		Iterator keyIter = prop.keySet().iterator(); 
		while(keyIter.hasNext()) {
			String command = (String) keyIter.next();
			String handleClassName = prop.getProperty(command);
			
			try {
				Class<?> handlerClass = Class.forName(handleClassName);
				CommandHandler handlerInstance = (CommandHandler) handlerClass.newInstance(); // newInstacne() :: 싱글톤과 유사.해당 클래스 내부의 변수, 메소드를 사용하겠다!!
				commandHandlerMap.put(command, handlerInstance);
				
				/*
				 * handlerMap 예시
				 * command => String 타입 "/insert.do"
				 * handlerInstance => member.command.insertHandler의 인스턴스
				 */
				
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				throw new ServletException(e); // 예외 강제 발생
			}
		}
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	
	//2
	private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		/*
		 * String requestURI = request.getRequestURI(); // 프로젝트 + 파일 경로 >> /Ch18_MVC2/insert.do
		 * String contextPath = request.getContextPath(); // 프로젝트 Path >> /Ch18_MVC2
		 * String command = requestURI.substring(contextPath.length()); // >> /insert.do
		 */
		
		String command = request.getRequestURI(); // 프로젝트 + 파일 경로
		System.out.println(command);
		
		/*
		 * 예시
		 * command = /Ch18_MVC2/insert.do
		 * request.getContextPath() = /Ch18_MVC2
		 * 
		 * command.indexOf(request.getContextPath())==0
		 * 프로젝트 + 파일경로의 시작이 getContextPath를 통해 얻어온 프로젝트 경로로 시작한다.
		 * >> 맞게 잘 들어온 경우
		 */
		
		if(command.indexOf(request.getContextPath())==0) {
			command = command.substring(request.getContextPath().length());
			System.out.println(command);
			
			/*
			 * String.substring(int index num)
			 * - 인덱스 번호가 num인 값을 포함해서 그 뒷부분까지 싹 다 잘라서 새로운 string을 만든다.
			 * 
			 * 예시
			 * 
			 * 기존 command = /Ch18_MVC2/insert.do
			 * request.getContextPath().length() = 10
			 * 
			 * command.substring(request.getContextPath().length())
			 * >> command.substring(10)
			 * >> 
			 * 
			 * 바뀐 command = /insert.do
			 */
		}
		
		CommandHandler handler = commandHandlerMap.get(command);
		
		if(handler==null) {
			handler = new NullHandler();
		}
		String viewPage = null;
		try {
			viewPage = handler.process(request, response);
			
			/*
			 *  각 핸들러에 맞는 인스턴스로 가서 process를처리한다.
			 *  아직 각 핸들러의 내용이 처리되지 않아 process 내용 처리 X
			 */
			
		} catch(Throwable e) {
			throw new ServletException(e);
		}
		
		/*
		 * 각 핸들러를 처리하고 나면 viewPage에 not null이 된다.
		 * viewPage값과 같은 jsp파일로 forward를 통해 데이터를 넘긴다.
		 */
		
		if(viewPage!=null) {
			// join.do를 사용한 경우 form에서 입력해서 넘어온 파라미터들이 다시 넘어간다.
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}
	}

}
