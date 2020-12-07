package auth.service;

/*
 * 세션 저장을 위한 User 클래스
 * 
 * DTO
 * - 계층 간 데이터 교환을 위한 자바빈즈
 */

public class User {

	private String id;
	private String name;
	
	public User(String id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	
	
}
