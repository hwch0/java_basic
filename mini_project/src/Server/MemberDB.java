package Server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import Member.Member;
import Member.Room;

public class MemberDB  {
	// DB 접근과 관련된 CRUD 기능 구현
	// 서버에서 String으로 넘겨주는 데이터를 JSON 혹은 객체 타입으로 변환 후 File로 저장
	// 서버에서 요청하는 데이터를 객체 -> JSON -> String으로 변환하여 전달
	final String fileName = "C:/Users/KOSA/Temp/member.db";
	final String fileName2 = "C:/Users/KOSA/Temp/memberNew.db";
	final String fileName3 = "C:/Users/KOSA/Temp/chatRooms.db";
	final File file = new File(fileName);
	final File file2 = new File(fileName2);
	final File file3 = new File(fileName3);
	ObjectOutputStream oos;
	ObjectInputStream ois;

								/*DB 접근용 함수*/
	// 채팅방 목록을 파일 객체로 저장
	public void saveRoomList(int roomCnt, Room newRoom) {
		Map<Integer, Room> roomMap = new HashMap<>();
		roomMap.put(roomCnt, newRoom);
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file3)));
			oos.writeObject(roomMap);
			oos.close();
		} catch (IOException e) {
			System.out.println("saveRoomList" + " 예외발생");
			e.printStackTrace();
		}
	}
	
	
	// 회원정보 리스트를 파일 객체로 저장 (사용)
	public void saveMemberList(Map<String, Member> memberMap) {
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
			oos.writeObject(memberMap);
			oos.close();
		} catch (IOException e) {
			System.out.println("saveFileMemberList" + " 예외발생");
			e.printStackTrace();
		}
	}
	// json으로 받은 memberMap을 다시 Hashmap으로 변환해서 파일로 저장
	public void saveMemberList(JSONObject memberMap) {
//		System.out.println("saveMemberList() 메서드 실행");
//        JSONObject jsonObject = new JSONObject(memberMap);
		
		Map<String, Member> hashMap = jsonToHash(memberMap);
		
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName2)));
			oos.writeObject(hashMap);
			oos.close();
		} catch (IOException e) {
			System.out.println("saveFileMemberList2" + " 예외발생");
			e.printStackTrace();
		}
	}
	
	public void saveMember(JSONObject member) {
		Member newMember = Member.makeMember(member);
		try {
			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
			oos.writeObject(newMember);
			oos.close();
		} catch (IOException e) {
			System.out.println("saveFileMember" + " 예외발생");
			e.printStackTrace();
		}
		
	}

	public Member findMember(String uid) {
		
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
			Map<String, Member> memberMap = (Map<String, Member>) ois.readObject();
			Collection<Member> memberList = memberMap.values();
			for(Member m : memberList) {
				if(uid.equals(m.getUid())) {
					return m;
				} 
			}
		} catch (IOException|ClassNotFoundException e) {
			System.out.println("findMember" + " 예외 발생");
			e.printStackTrace();
		} 
		
		return null;

	}
	
	public Member findMember(JSONObject jsonObject) {
		String targetId = jsonObject.getString("uid");
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
			Map<String, Member> memberMap = (Map<String, Member>) ois.readObject();
			Collection<Member> memberList = memberMap.values();
			for(Member m : memberList) {
				if(targetId.equals(m.getUid())) {
					return m;
				} 
			}
		} catch (IOException|ClassNotFoundException e) {
			System.out.println("findMember" + " 예외 발생");
			e.printStackTrace();
		} 
		
		return null;
		
	}

	public void readAllMembers() {
		try {
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
			Map<String, Member> memberMap = (Map<String, Member>) ois.readObject();
			Collection<Member> memberList = memberMap.values();
			
			System.out.println("----------------회원 목록 조회----------------");
			for(Member m : memberList) {
				System.out.println(m.toString());
			}
			System.out.println("--------------------------------------------");
			
		} catch (Exception e) {
			System.out.println("loadFileMemberList" + " 예외발생");
			e.printStackTrace();
		}
		
	}
	
	public Map<String, Member> getAllMembers() {
		ObjectInputStream ois;
		Map<String, Member> memberMap = null;
		try {
			if(file.exists() && file.length() > 0) {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
				
				
				memberMap = (Map<String, Member>) ois.readObject();
				System.out.println(memberMap.toString());
			} else {
				System.out.println("멤버 목록 없음");
				memberMap = new HashMap<String, Member>();
			}
			
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("getAllMembers" + " 예외발생");
			e.printStackTrace();
		}
		
		return memberMap;
		
	}

	public void updateMember(String field) {
		// TODO Auto-generated method stub

	}

	public void deleteMember(String uid) {
		// TODO Auto-generated method stub

	}
	
									/* 서비스용 함수 */
	
	// 회원정보 리스트 json -> Map 변환
	public Map<String, Member> jsonToHash(JSONObject jsonObject) {
		
		Map<String, Member> hashMap = new HashMap<>();
		
		for (String key : jsonObject.keySet()) {
			String id = key;
			Member member = Member.makeMember(jsonObject.getJSONObject(key));
			hashMap.put(id, member);
		}
		return hashMap;
	}
	
	// 로그인: 아이디, 비밀번호 확인 후 member 객체 전달 (사용)
	public Member checkMemberInfo(JSONObject jsonObject) {
		Member targetMember = findMember(jsonObject);
		
		if((targetMember!=null) 
				&& (targetMember.getUid().equals(jsonObject.getString("uid"))) 
				&& (targetMember.getPwd().equals(jsonObject.getString("pwd")))) {
			System.out.println("DB에서 멤버 찾음 " + targetMember.toString());
			return targetMember;
		}
		System.out.println("DB에서 멤버 못 찾음");
		return null;
	}
	
	// 비밀번호 찾기: 아이디, 전화번호 확인 후 비밀번호 전달
	public String findPassword(JSONObject jsonObject) {
		Member targetMember = findMember(jsonObject);
		
		if((targetMember!=null) 
				&& (targetMember.getUid().equals(jsonObject.getString("uid"))) 
				&& (targetMember.getPhoneNumber().equals(jsonObject.getString("phoneNumber")))) {
			return targetMember.getPwd();
		}
		return "";
	}

}
