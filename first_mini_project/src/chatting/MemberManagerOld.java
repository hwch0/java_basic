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

import Member.Member;
import Server.MemberDB;

public class MemberManagerOld {
	// MemberDB 객체 생성
	static final MemberDB memberDB = new MemberDB();
	
	// 회원관리할 Map 컬렉션 생성 (static 멤버 변수)
	static Map<String, Member> memberMap = new HashMap<>();
	Member currentMember;
	
	public void choiceMenu() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = true;
		
		while(exit) {
			displayMenuBeforeLogin();
			int num = scanner.nextInt();
			switch (num) {
			case 1 -> insertMember(scanner); 
			case 2 -> login(scanner);
			case 3 -> findPwd(scanner);
			case 4 -> exit = false;
			}
		} System.out.println("프로그램이 종료 되었습니다.");
	}
	
	public void choiceMenuAfterLogin() {
		Scanner scanner = new Scanner(System.in);
		boolean exit = true;
		while(exit) {
			displayMenuAfterLogin();
			int num = scanner.nextInt();
			switch (num) {
			case 1 -> updateInfo(scanner); 
			case 2 -> createChattingRoom(scanner);
			case 3 -> enterChattingRoom(scanner);
			case 4 -> deleteMember(scanner);
			case 5 -> {
				exit = false;
				logout(); 
//				choiceMenu();
			}
			}
		}
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
			choiceMenuAfterLogin();
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
		System.out.println("이름 >>> ");
		String name = scanner.next();
		
		Member findMember = memberDB.findMember(uid);
		if((findMember != null) && (name.equals(findMember.getName()))) {
			System.out.println(uid + "님의 비밀번호는 [" + findMember.getPwd() + "] 입니다.");
		} else {
			System.out.println("존재하지 않는 회원입니다.");
		}
	}

	public void login(Scanner scanner) {
		System.out.println("[로그인]");
		System.out.println("아이디 >>> ");
		String uid = scanner.next();
		System.out.println("비밀번호 >>> ");
		String pwd = scanner.next();
		
		Member findMember = memberDB.findMember(uid);
		
		if((findMember != null) && (pwd.equals(findMember.getPwd()))) {
			System.out.println("안녕하세요" + uid +" 님 로그인 되었습니다.");
			currentMember = findMember;
			choiceMenuAfterLogin();
		} else if(findMember != null) {
			System.out.println("비밀번호 입력 오류");
			choiceMenu();
		} else {
			System.out.println("존재하지 않는 사용자 입니다.");
			choiceMenu();
		}
	}


	public void displayMenuBeforeLogin() {
		System.out.println("1. 회원가입"); // 파일로 저장
		System.out.println("2. 로그인");
		System.out.println("3. 비밀번호 찾기");
		System.out.println("4. 종료");
		System.out.println("번호를 입력하세요 >>> ");
	}
	
	public void displayMenuAfterLogin() {
		System.out.println("1. 정보수정");
		System.out.println("2. 채팅방 생성");
		System.out.println("3. 채팅방 입장"); // 목록보기
		System.out.println("4. 회원탈퇴"); 
		System.out.println("5. 로그아웃"); 
		System.out.println("번호를 입력하세요 >>> ");
	}
	
	public void displayMenuForManager() {
		System.out.println("1. 회원목록 보기");
		System.out.println("2. 로그아웃");
		
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
		
		memberMap.put(uid, new Member(uid, name, pwd, age, phoneNumber, address, gender, false));
		System.out.println("회원가입이 완료 되었습니다.");
		System.out.println(memberMap.toString());
		System.out.println();
		memberDB.saveMemberList(memberMap);
		memberDB.readAllMembers();
		
//		saveFileMemberList();
//		loadFileMemberList();
		
	}
	
//	public void saveFileMemberList() {
//		String fileName = "C:/Users/KOSA/Temp/member.db";
//		File file = new File(fileName);
//		ObjectOutputStream oos;
//		try {
//			oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
//			oos.writeObject(memberMap);
//			oos.close();
//		} catch (IOException e) {
//			System.out.println("saveFileMemberList" + " 예외발생");
//		}
//	}
//	
//	public void loadFileMemberList() {
//		String fileName = "C:/Users/KOSA/Temp/member.db";
//		File file = new File(fileName);
//		try {
//			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
//			Map<String, Member> memberMap = (Map<String, Member>) ois.readObject();
//			Collection<Member> memberList = memberMap.values();
//			
//			for(Member m : memberList) {
//				System.out.println(m.toString());
//			}
//			
//		} catch (Exception e) {
//			System.out.println("loadFileMemberList" + " 예외발생");
//			e.printStackTrace();
//		}
//		
//	}
	

}
