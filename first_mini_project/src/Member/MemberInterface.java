package Member;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import chatting.Member;

public interface MemberInterface {
	// DB관련 CRUD
	// 추후 회원을 일반회원과 관리자 등으로 나눌 수 있기 때문에 db 접근과 관련된 CRUD기능은 interface로 일반화
	// 추후 회원 타입별로 구현
//	public void insertMember(Member member);
	
	public void saveMemberList(Map<String, Member> memberMap);
	public void saveMemberList(JSONObject jsonObject);
	
	public Member findMember(String uid);
	public Member findMember(JSONObject jsonObject);
	
	public void readAllMembers();
	
	public void updateMember(String field);
	
	public void deleteMember(String uid);
}
