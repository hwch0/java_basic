package Client;

import java.util.Scanner;

public class Display {
	
	public static void chatting() {
		System.out.println();
		System.out.println("----------------------------------");
		System.out.println("           ChattingRoom          ");
		System.out.println("----------------------------------");
	}
	
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
		System.out.println("7. 채팅방 목록"); 
		System.out.println("8. 회원탈퇴"); 
		System.out.println("9. 모든 회원목록 보기"); 
		System.out.println("10. 로그아웃"); 
		System.out.println("11. 프로그램 종료"); 
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
	
}
