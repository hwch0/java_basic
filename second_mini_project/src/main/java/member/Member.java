package member;

import java.io.Serializable;
import java.util.Objects;

import lombok.Data;

@Data
public class Member implements Serializable {
	// 객체를 파일로 저장하기 위한 serial 번호
	private static final long serialVersionUID = -7958345196310544448L;
	
	private String uid; // 객체식별을 위한 고유한 키
	private String name;
	private String pwd;
	private int age;
	private String phoneNumber;
	private String address;
	private String gender; // 'F', 'M'
	private Boolean type; // true or false
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(uid);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Member other = (Member) obj;
		return Objects.equals(uid, other.uid);
	}
	
	public Member(String uid, String name, String pwd, int age, String phoneNumber, String address, String gender, boolean type) {
		super();
		this.uid = uid;
		this.name = name;
		this.pwd = pwd;
		this.age = age;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.gender = gender;
		this.type = type;
	}

	public Member() {
		// TODO Auto-generated constructor stub
	}
	
	

}
