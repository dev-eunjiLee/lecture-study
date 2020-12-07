package member.service;

import java.util.Map;

/*
 * JoinService가 회원 가입 기능을 구현할 때 필요한 요청 데이터를 담는 클래스
 * >> DTO(O)
 */

public class JoinRequest {
	
	private String id;
	private String name;
	private String password;
	private String confirmPassword;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
	public boolean isPasswordEqualToConfirm() {
		return password!=null && password.equals(confirmPassword);
		
		/*
		 * 객체에 담겨있는 password가 not null이고, password가 confirmPassword와 같으면 true를 리턴(그외는 false)
		 * >> password 필드와 confirmPassword가 같은 값인지 확인
		 */
	}
	
	/*
	 * 각 필드의 데이터가 유효한지 검사
	 * errors 맵 객체에 에러 정보를 담는다.
	 */
	
	public void validate(Map<String, Boolean> errors) {
		
		// null 여부, empty 여부 체크
		// 빈 필드가 있는 경우 true가 value로 들어간다. > 유효한 값인 경우 해당 필드를 key로 하는 map 값이 없다.
		checkEmpty(errors, id, "id");
		checkEmpty(errors, name, "name"); 
		checkEmpty(errors, password, "password"); 
		checkEmpty(errors, confirmPassword, "confirmPassword");
		
		// error객체에 confirmPassword가 없고, password와 confirmPassword가 다른 경우
		if(!errors.containsKey("confirmPassword")) {
			if(!isPasswordEqualToConfirm()) {
				errors.put("notMatch", Boolean.TRUE);
			}
		}
		
	}
	
	private void checkEmpty(Map<String, Boolean> errors, String value, String fieldName) {
		
		/*
		 * value가 null이거나 비어있으면
		 * 파라미터로 들어온 Map타입 error에 <fieldName, TRUE>를 추가한다.
		 * 
		 * >> 해당 value가 비었는지 안비었는지를 체크하여 비었으면 true를 추가한다.
		 */
		
		if(value==null || value.isEmpty()) {
			errors.put(fieldName, Boolean.TRUE);
		}
	}
	
	

}
