package chatting;

import java.util.Scanner;

public class Display {
	
	public static void displayMenu() {
		System.out.println();
		System.out.println("-------------------------");
		System.out.println("           Menu          ");
		System.out.println("-------------------------");
		System.out.println("1. 회원가입");
		System.out.println("2. 로그인");
		System.out.println("3. 비밀번호 찾기");
		System.out.println("4. 정보수정");
		System.out.println("5. 채팅방 생성");
		System.out.println("6. 채팅방 입장");
		System.out.println("7. 회원탈퇴"); 
		System.out.println("8. 모든 회원목록 보기"); 
		System.out.println("9. 로그아웃"); 
		System.out.println("10. 프로그램 종료"); 
		System.out.println("-------------------------");
		System.out.print("번호를 입력하세요 >>> ");
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
		System.out.println("5. 모든 회원목록 보기"); 
		System.out.println("6. 로그아웃"); 
		System.out.println("번호를 입력하세요 >>> ");
	}
	
//	public void displayMenu() {
//		System.out.println("1. 회원가입"); // 파일로 저장
//		System.out.println("2. 로그인");
//		System.out.println("3. 비밀번호 찾기");
//		System.out.println("4. 정보수정");
//		System.out.println("5. 채팅방 생성");
//		System.out.println("6. 채팅방 입장");
//		System.out.println("7. 회원탈퇴"); // 목록보기
//		System.out.println("8. 모든 회원목록 보기"); 
//		System.out.println("9. 로그아웃"); 
//		System.out.println("10. 프로그램 종료"); 
//		System.out.println("번호를 입력하세요 >>> ");
//	}
	
	
//	public void choiceMenuAfterLogin(String uid) {
//		setCurrentId(uid);
//		Scanner scanner = new Scanner(System.in);
//		boolean exit = true;
//		while(exit) {
//			displayMenuAfterLogin();
//			int num = scanner.nextInt();
//			switch (num) {
//			case 1 -> updateInfo(scanner); 
//			case 2 -> createChattingRoom(scanner);
//			case 3 -> enterChattingRoom(scanner);
//			case 4 -> deleteMember(scanner);
//			case 5 -> deleteMember(scanner);
//			case 6 -> {
//				exit = false;
//				logout(); 
//				System.out.println("로그아웃 되었습니다.");
////				choiceMenu();
//			}
//			}
//		}
//	}
	

}
