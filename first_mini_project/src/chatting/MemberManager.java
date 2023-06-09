package chatting;
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
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

import Client.ChatClient;
import Member.Member;
import Server.MemberDB;
import lombok.Data;

@Data
public class MemberManager {
	// MemberDB 객체 생성
	static final MemberDB memberDB = new MemberDB();
	
	// 회원관리할 Map 컬렉션 생성 (static 멤버 변수)
//	static Map<String, Member> memberMap = new HashMap<>();
	static Map<String, Member> memberMap = memberDB.getAllMembers();
	ChatClient connectedMember;
	public Member currentMember = null;
	String currentId = "";
	
	public MemberManager(ChatClient connectedMember) {
		this.connectedMember = connectedMember;
	}
	
	public void choiceMenu() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = true;
		
		while(exit) {
			displayMenu();
			int num = scanner.nextInt();
			switch (num) {
			case 1 -> insertMember(scanner); 
			case 2 -> login(scanner);
			case 3 -> findPwd(scanner);
			case 4 -> updateInfo(scanner); 
			case 5 -> createChattingRoom(scanner);
			case 6 -> enterChattingRoom(scanner);
			case 7 -> deleteMember(scanner);
			case 8 -> printAllmembers(scanner);
			case 9 -> {
				exit = false;
				logout(); 
				System.out.println("로그아웃 되었습니다.");
//				choiceMenu();
			}
			case 10 -> {
				try {
					connectedMember.unconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
				exit = false;
			}
			}
		} System.out.println("프로그램이 종료 되었습니다.");
	}
	
	private void printAllmembers(Scanner scanner) {
		// memberMap json으로 변환
        // JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "getAllMembers");
//        jsonObject.put("data", newMember);
		
        // 서버로 JSON 객체 전송
        connectedMember.send(jsonObject);
	}

	
	private void logout() {
		currentMember = null;
	}

	private void deleteMember(Scanner scanner) {
		System.out.println("[회원탈퇴]");
		System.out.println("정말로 탈퇴 하시겠습니까? (y/n)");
		
		String answer = scanner.next();
		if(answer.equals("y")) {
			memberMap.remove(currentMember.getUid());
			memberDB.saveMemberList(memberMap);
		} else {
//			choiceMenuAfterLogin(currentId);
		}
	}

	private Object enterChattingRoom(Scanner scanner) {
		// TODO Auto-generated method stub
		return null;
	}

	private Object createChattingRoom(Scanner scanner) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void memberInfo() {
		if(currentMember != null) {
			System.out.println("1. 아이디 : " + currentMember.getUid());
			System.out.println("2. 이름 : " + currentMember.getName());
			System.out.println("3. 비밀번호 : " + currentMember.getPwd());
			System.out.println("4. 나이 : " + currentMember.getAge());
			System.out.println("5. 전화번호 : " + currentMember.getPhoneNumber());
			System.out.println("6. 주소 : " + currentMember.getAddress());
			System.out.println("7. 성별 : " + currentMember.getGender());
		} else {
			System.out.println("로그인 하세요.");
		}
	}

	private void updateInfo(Scanner scanner) {
		boolean exit = true;
		while(exit) {
			System.out.println("[회원정보 수정]");
			// 현재 정보 출력
			memberInfo();
			System.out.println("수정을 원하시는 항목의 번호를 입력해주세요. (1~7번)");
			System.out.println("저장 후 종료를 원하실 경우 8번을 눌러주세요 >>> ");
			// 수정하고 싶은 항목 번호 입력
			int num = scanner.nextInt();
			switch (num) {
			case 1 -> {
				System.out.println("아이디는 수정 불가합니다!!");
			}
			case 2 -> {
				System.out.println("이름 수정 >>> ");
				String newinfo = scanner.next();
				currentMember.setName(newinfo);
			}
			case 3 -> {
				System.out.println("비밀번호 수정 >>> ");
				String newinfo = scanner.next();
				currentMember.setPwd(newinfo);
			}
			case 4 -> {
				System.out.println("나이 수정 >>> ");
				int newinfo = scanner.nextInt();
				currentMember.setAge(newinfo);
			}
			case 5 -> {
				System.out.println("전화번호 수정 >>> ");
				String newinfo = scanner.next();
				currentMember.setPhoneNumber(newinfo);
			}
			case 6 -> {
				System.out.println("주소 수정 >>> ");
				String newinfo = scanner.next();
				currentMember.setAddress(newinfo);
			}
			case 7 -> {
				System.out.println("성별 수정 >>> ");
				String newinfo = scanner.next();
				currentMember.setGender(newinfo);
			}

			case 8 -> {
				memberDB.saveMemberList(memberMap);
				System.out.println("수정된 정보가 DB에 반영되었습니다.");
				exit = false;
//				choiceMenuAfterLogin();
			}
			}
		}
	}

	public void findPwd(Scanner scanner) {
		// Id, 이름 입력 받으면 password 알려주기
		System.out.println("[비민번호 찾기]");
		System.out.println("아이디 >>> ");
		String uid = scanner.next();
		System.out.println("핸드폰 번호 >>> ");
		String phoneNumber = scanner.next();
		
        // 찾을 ID를 서버로 전송하여 가입여부 확인 JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        JSONObject memberInfo = new JSONObject();
        memberInfo.put("uid", uid);
        memberInfo.put("phoneNumber", phoneNumber);
        jsonObject.put("command", "findPwd");
        jsonObject.put("data", memberInfo);
		
        // 서버로 JSON 객체 전송
        connectedMember.send(jsonObject);
		
		
//		Member findMember = memberDB.findMember(uid);
//		if((findMember != null) && (name.equals(findMember.getName()))) {
//			System.out.println(uid + "님의 비밀번호는 [" + findMember.getPwd() + "] 입니다.");
//		} else {
//			System.out.println("존재하지 않는 회원입니다.");
//		}
	}

	public void login(Scanner scanner) {
		if (!currentId.equals("")) {
			System.out.println("현재 로그인된 상태 입니다. 로그인 아이디: " + currentId);
		} else {
			System.out.println("[로그인]");
			System.out.println("아이디 >>> ");
			String uid = scanner.next();
			System.out.println("비밀번호 >>> ");
			String pwd = scanner.next();
			
			// 찾을 ID를 서버로 전송하여 가입여부 확인 JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			JSONObject memberInfo = new JSONObject();
			memberInfo.put("uid", uid);
			memberInfo.put("pwd", pwd);
			jsonObject.put("command", "checkMemberInfo");
			jsonObject.put("data", memberInfo);
			
			System.out.println("checkMemberInfo json 변환 확인 : " + jsonObject.toString());
			
			// 서버로 JSON 객체 전송
			connectedMember.send(jsonObject);
		}
		
		if(currentId.equals("")) {
			System.out.println("존재하지 않는 사용자 입니다.");
		} else {
        	System.out.println("안녕하세요. " + currentId +" 님^^ 로그인 되었습니다!");
		}
        
//        if(currentMember != null) {
//        	System.out.println("안녕하세요" + uid +" 님^^ 로그인 되었습니다!");
//        	choiceMenuAfterLogin();
//        } else if(currentMember == null) {
//			System.out.println("존재하지 않는 사용자 입니다.");
//			choiceMenu();
//        } else {
//        	System.out.println("로그인 예외 발생");
//        	choiceMenu();
//        }
		
		
//		if((findMember != null) && (pwd.equals(findMember.getPwd()))) {
//			System.out.println("안녕하세요" + uid +" 님 로그인 되었습니다.");
//			currentMember = findMember;
//			choiceMenuAfterLogin();
//		} else if(findMember != null) {
//			System.out.println("비밀번호 입력 오류");
//			choiceMenu();
//		} else {
//			System.out.println("존재하지 않는 사용자 입니다.");
//			choiceMenu();
//		}
	}


	public void displayMenu() {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("           Menu          ");
		System.out.println("-------------------------");
		System.out.println("1. 회원가입"); // 파일로 저장
		System.out.println("2. 로그인");
		System.out.println("3. 비밀번호 찾기");
		System.out.println("4. 정보수정");
		System.out.println("5. 채팅방 생성");
		System.out.println("6. 채팅방 입장");
		System.out.println("7. 회원탈퇴"); // 목록보기
		System.out.println("8. 모든 회원목록 보기"); 
		System.out.println("9. 로그아웃"); 
		System.out.println("10. 프로그램 종료"); 
		System.out.println("-------------------------");
		System.out.print("번호를 입력하세요 >>> ");
	}
	
	public void printRooms() {
		// 채팅방 리스트 보여주기
		
		// 채팅방 번호 입력 받기 (해당 소켓 포트넘버 받아오기)
		System.out.println("번호를 입력하세요 >>> ");
		
		// 포트번호 받아서 클라이언트(소켓) 생성하기
		
	}
	
	public void insertMember(Scanner scanner) {
		System.out.println("[회원가입]");
		
		System.out.print("아이디: ");
		final String uid = scanner.next();
		
		// 입력받은 아이디가 존재하는지 확인하는 코드 필요
		System.out.print("이름: ");
		final String name = scanner.next();
		
		System.out.print("비밀번호: ");
		final String pwd = scanner.next();
		System.out.print("나이: ");
		final int age = scanner.nextInt();
		System.out.print("전화번호: ");
		final String phoneNumber = scanner.next();
		System.out.print("주소: ");
		final String address = scanner.next();
		System.out.print("성별: ");
		final String gender = scanner.next();
		
//		memberMap.put(uid, new Member(uid, name, pwd, age, phoneNumber, address, gender));
		Member newMember = new Member(uid, name, pwd, age, phoneNumber, address, gender, false);
		System.out.println("회원가입이 완료 되었습니다.");
		System.out.println(memberMap.toString());
		System.out.println();
		
//		connectedMember.send("saveMemberList" : "memberMap");
//		connectedMember.send("printMemberList" : "memberMap");
		
		// memberMap json으로 변환
        // JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "saveMember");
        jsonObject.put("data", newMember);
        System.out.println("insertMember json 변환 확인 : " + jsonObject.toString());
		
        // 서버로 JSON 객체 전송
        connectedMember.send(jsonObject);
        
//		memberDB.saveMemberList(memberMap);
//		memberDB.readAllMembers();
}
}
