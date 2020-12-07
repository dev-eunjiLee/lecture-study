package member.model;

import java.util.Date;

/*
 *  데이터베이스에서 가져온 Member 테이블의 데이터를 담는,
 *  데이터베이스의 Member테이블로 넣을 데이터를 담는
 *  DTO
 */

public class Member {
	
	private String id;
	private String name;
	private String password;
	private Date regDate;
	
	public Member(String id, String name, String password, Date regDate) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.regDate = regDate;
	}

	/*
	 * getter만 생성
	 */
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public Date getRegDate() {
		return regDate;
	}
	
	/*
	 * matchPassword(String pwd)
	 *  - 입력한 파라미터 pwd가 기존 Member DTO에 있는 password와 같으면 true return
	 */
	
	public boolean matchPassword(String pwd) {
		return password.equals(pwd);
	}
	
	/*
	 * password 변경 메소드
	 */
	public void chanagePassword(String newPwd) {
		this.password = newPwd;
	}

	
	
	

}
