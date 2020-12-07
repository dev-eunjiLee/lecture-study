package jdbc;

import java.io.IOException;
import java.io.StringReader;
import java.sql.DriverManager;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/*
 * ServletContextListener 인터페이스
 *  - 웹 어플리케이션이 시작되거나 종료될 때 호출할 메서드를 정의한 인터페이스
 *  - 메소드
 *  	1. contextInitialized(ServletContextEvent sce): 웹 어플리케이션 초기화할 때 호출
 *  	2. contextDestroyed(ServletContextEvent sce): 웹 어플리케이션 종료할 때 호출
 *  
 *  사용 방법
 *  - 작성 후 annotation or web.xml등록 방법을 이용해 등록한다.
 * 
 */

public class DBCPInitListener implements ServletContextListener {

	/*
	 *  웹 어플리케이션 초기화할 때 호출
	 *  
	 *  - web.xml에서 입력한 초기 파라미터 값에서 ConnectionPoolConfig에 사용할 값들을 불러온다.
	 *  - 초기 파라미터에서 입력한 poolconfig 내용을 담고 있는 Properties객체를 이용해
	 *    JDBCDriver를 load하고 ConnectionPool을 만들어서 끌어온다.
	 */
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String poolConfig = sce.getServletContext().getInitParameter("poolConfig");
		Properties prop = new Properties();
		
		try {
			prop.load(new StringReader(poolConfig)); //  초기 파라미터에서 읽어온 값 로드
		} catch(IOException e) {
			throw new RuntimeException("config load fail",e);
		}
		
		loadJDBCDriver(prop);
		initConnectionPool(prop);
		 
	}
	
	/*
	 * Properties 객체를 활용해서 JDBCDriver를 load
	 */
	
	private void loadJDBCDriver(Properties prop) {
		String driverClass = prop.getProperty("jdbcDriver");
		
		try {
			Class.forName(driverClass);
		} catch(ClassNotFoundException ex) {
			throw new RuntimeException("fail to load JDBCDriver", ex);
		}
	}
	
	/*
	 * Properties 객체를 활용해서 ConnectionPool 만들기
	 * 
	 * 여기서는 commons를 이용해서 커넥션 풀을 생성했지만, 요즘 방식은 tomcat을 이용해서 작성한다.
	 * - tomcat, jndi, context.xml로 커넥션 풀 등록 방법: Ch18_MVC2.EmpDAO, com.controller.EmpSelectpoolAnno
	 */
	
	private void initConnectionPool(Properties prop) {
		
		
		try {
			/*
			 * db로그인할 url, name, pw 불러오기
			 */
			
			String jdbcUrl = prop.getProperty("jdbcUrl");
			String username = prop.getProperty("dbUser");
			String pw = prop.getProperty("dbPass");
			
			/*
			 * ConnectionFactory
			 * 
			 * 커넥션 풀이 새로운 커넥션을 생성할  때 사용할 커넥션 팩토리를 생성
			 * 오라클과 연결할 url, name, pw를 생성자에 넣어 객체를 생성한다.
			 */
			
			ConnectionFactory connFactory = 
					new DriverManagerConnectionFactory(jdbcUrl, username, pw);
			
			/*
			 * PoolableConnectionFactory
			 * 
			 * PoolConnection을 생성하는 팩토리를 생성한다.
			 * DBCP는 커넥션 풀에 커넥션을 보관할 때 PoolableConnection을 사용한다.
			 * 이 클래스는 내부적으로 실제 커넥션을 담고 있으며, 커넥션 풀을 관리하는 데 필요한 기능을 추가로 제공한다.
			 * 커넥션 풀에서 가져온 커넥션에 close() 메소드를 실행할 경우 닫는 것이 아니라 해당 커넥션을 풀로 반환한다.
			 * 
			 * DBMS에 따라 권장되는 쿼리문이 다르다.
			 * Oracle: select 1 from dual
			 * MySQL: select 1
			 */
			
			PoolableConnectionFactory poolableConnFactroy =
					new PoolableConnectionFactory(connFactory, null);
			String validationQuery = prop.getProperty("validationQuery");
			
			if(validationQuery!=null && !validationQuery.isEmpty()) {
				
				/*
				 * web.xml의 초기 파라미터를 이용해 만들어진 Properties 객체에
				 * validationQuery키를 이용해 가져온 값이 null아니고 비어있지 않은 경우
				 * 해당 조건문 수행
				 * 
				 * setsValidationQuery("select 1") : 검사용 쿼리 설정(검사X 쿼리설정O)
				 * 
				 * 커넥션은 일정 시간이 지나면 종료된다. 하지만 여전히 커넥션 풀은 해당 커넥션을 가지고 있는 경우가 있는데, 이 경우 에러가 발생한다.
				 * 그래서 이 때 connection이 정상인지 확인하는 검사가 필요
				 * 단순 조회용이기 때문에 가장 간단한 쿼리를 설정
				 */
				
				poolableConnFactroy.setValidationQuery(validationQuery);
				
			}
			
			/*
			 *  GenericObjectPoolConfig
			 *  
			 *  커넥션 풀의 설정 정보를 생성한다.
			 *  
			 *  메소드
			 *  setTimeBetweenEvictionRunsMillis(1000L*60L*5L): 풀에 있는 유휴 커넥션 검사 주기를 설정한다. 양수가 아니면 검사하지 않는다.
			 *  setTestWhileIdle(ture): 풀에 보관중인 커넥션이 유효한지 검사할지 여부 > true면 설정된 주기마다 검사 실행
			 *  setMinIdle: 최소로 유지할 커넥션 개수(기본값 0)
			 *  setMaxTotal(50): 최대로 생성할 Connection 수
			 */
					
			GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
			poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 5L);
			poolConfig.setTestWhileIdle(true);
			int minIdle =  getIntProperty(prop, "minIdle", 5); // getIntProperty는 아래에 직접 만든 메소드
			poolConfig.setMinIdle(minIdle);
			int maxTotal = getIntProperty(prop, "maxTotal", 50);
			poolConfig.setMaxTotal(maxTotal);
			
			/*
			 * GenericObjectPool
			 * 
			 * 임의의 오브젝트에게 견고한 풀을 기능적으로 제공한다.
			 * (여기서는 PoolableConnection을 위한 풀링을 제공한다.) >> 커넥션 풀 생성
			 * 
			 * new GenericObjectPool<PoolableConnection>(생성자 파라미터1, 생성자 파라미터2)
			 * 파라미터1: 커넥션 풀을 생성할 때 사용할 팩토리
			 * 파라미터2: 커넥션 풀 설정
			 * 
			 * poolableConnFactroy.setPool(connectionPool)
			 * 생성된 커넥션풀을 PoolableConnectionFactory에 연결한다.
			 */
			
			GenericObjectPool<PoolableConnection> connectionPool = 
					new GenericObjectPool<PoolableConnection>(poolableConnFactroy, poolConfig);
			poolableConnFactroy.setPool(connectionPool);
			
			/*
			 * 커넥션 풀을 제공하는 jdbc드라이버 등록
			 */
			
			Class.forName("org.apache.commons.dbcp2.PoolingDriver");
			PoolingDriver driver =
					(PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
			
			/*
			 * 위에서 등록한 커넥션 풀 드라이버에 생성한 커넥션 풀을 등록한다.
			 */
			
			String poolName = prop.getProperty("poolName");		
			driver.registerPool(poolName, connectionPool);
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
}

	/*
	 * Properties 객체에 있는 value값이 숫자인 경우, 숫자로 치환해주는 메소드
	 * 만약, 값이 비어있다면 메소드 파라미터로 입력해준 defaultValue 값을 전달
	 */
	
	private int getIntProperty(Properties prop, String propName, int defaultValue) {
		String value = prop.getProperty("propName"); // "3"
		if(value==null) return defaultValue; // 5
		return Integer.parseInt(value); // 3
		
	}
	
	/*
	 * 웹 어플리케이션 종료할 때 호출 > 특별한 내용 없어서 공란으로 설정
	 */
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}
