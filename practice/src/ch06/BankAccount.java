package ch06;

import java.util.Scanner;


public class BankAccount {
	// Scanner: 화면으로부터 데이터를 입력받는 클래스 
	public Scanner sc = new Scanner(System.in);
	private Account [] accounts = new Account[100]; // 계좌 객체에 대해 저장하는 배열 (길이 = 100)
	private int count = 0; // 생성된 계좌 객체 개수 (accounts에 객체 저장시 인덱스로 활용)
		
	// 계좌 객체 생성
		public void createAccount() {
			System.out.println("------");
			System.out.println("계좌생성");
			System.out.println("------");
			
			// 계좌번호, 계좌주, 초기입금액을 사용자로부터 입력 받아 각각의 변수에 대입
			System.out.print("계좌번호: ");
			String number = sc.next();
			System.out.print("계좌주: ");
			String name = sc.next();
			System.out.print("초기입금액: ");
			int balance = sc.nextInt();
			
			// 위에서 입력받은 값을 매개변수로 Account 생성자를 사용하여 계좌 객체 생성
			Account newAccount = new Account(number, name, balance);
			// 생성된 계좌 객체를 accounts 배열에 저장
			accounts[count] = newAccount;
			count ++;
			System.out.println("결과: 계좌가 생성되었습니다.");
		}
		
		// 메뉴 목록 출력
		public void printMenu() {
			System.out.println("--------------------------------------------------------");
			System.out.println("1.계좌생성 | 2.계좌목록 | 3.예금 | 4.출금 | 5.계좌삭제 | 6. 종료");
			System.out.println("--------------------------------------------------------");
		}
		
		// 계좌 전체 조회 
		public void printAccounts() {
			System.out.println("------");
			System.out.println("계좌목록");
			System.out.println("------");
			
			// 반복문으로 생성된 계좌 객체에 대한 정보를 출력한다
			// 주의사항: 반복문으로 전체 계좌 조회 시, i값의 범위를 배열의 전체 길이(accounts.length)가 아니라 생성된 계좌의 개수(count)로 지정해줘야 한다
			// i의 범위를 accounts.length로 지정할 경우 배열 전체를 탐색하기 때문에 계좌가 100개 생성된게 아니면 java.lang.NullPointerException 발생
			for(int i = 0 ; i <count ; i++) {
				accounts[i].printAccount();
			}
			
		}
		// 예금 or 출금 시, 입력받은 계좌번호에 대한 객체가 있는지 확인하는 메서드
		// 반복문으로 생성된 계좌 객체의 계좌번호와 입력받은 계좌번호의 일치여부를 확인
		// 일치할 경우 해당 계좌 객체를 반환 (일치하는 계좌가 없을 경우 null 반환)
		public Account findAccount(String number) {
			for(int i = 0 ; i < count ; i++) {
				if (accounts[i].isAccount(number)) {
					return accounts[i];
				}
			} return null;
		}
		
		// 삭제할 계좌 인데스 찾기
		// 입력받은 계좌번호가 존재할 경우 해당 계좌의 인덱스를 정수 타입으로 반환
		// 존재하지 않을 경우 -1 반환
		public int findIndex(String number) {
			for(int i = 0 ; i < count ; i++) {
				if (accounts[i].isAccount(number)) {
					return i;
				}
			} return -1; 
		}
		
		// 입금
		public void deposit() {
			System.out.println("------");
			System.out.println("예금");
			System.out.println("------");
			
			System.out.print("계좌번호: ");
			String number = sc.next();
			System.out.print("입금액: ");
			int balance = sc.nextInt();
			
			// 입력받은 계좌번호와 일치하는 계좌가 있는지 탐색
			// 일치하는 계좌가 있을 경우 해당 객체가 참조변수에 대입되고, 없으면 null 대입
			Account account = findAccount(number);
			if(account != null) {
				account.incBalance(balance); // 계좌가 존재할 경우 해당 계좌의 잔액을 증가시키는 메서드를 호출하여 입금할 금액을 매개변수로 넘겨준다
				System.out.println("입금이 완료 되었습니다.");
			} else {
				System.out.println("존재하지 않는 계좌번호 입니다."); // 계좌가 존재하지 않을 경우(account == null) 안내 문구 출력
			}
		}
		
		// 출금
		public void withdraw() {
			System.out.println("------");
			System.out.println("출금");
			System.out.println("------");
			
			System.out.print("계좌번호: ");
			String number = sc.next();
			System.out.print("출금액: ");
			int balance = sc.nextInt();
			
			// 입력받은 계좌번호와 일치하는 계좌가 있는지 탐색
			// 일치하는 계좌가 있을 경우 해당 객체가 참조변수에 대입되고, 없으면 null 대입
			Account account = findAccount(number);
			if(account != null) {
				account.decBalance(balance); // 계좌가 존재할 경우 해당 계좌의 잔액을 감소시키는 메서드를 호출하여 출금할 금액을 매개변수로 넘겨준다
				System.out.println("출금이 완료 되었습니다.");
			} else {
				System.out.println("존재하지 않는 계좌번호 입니다."); // 계좌가 존재하지 않을 경우(account == null) 안내 문구 출력
			}

		}
		// 계좌삭제
		public void deleteAccount() {
			System.out.println("------");
			System.out.println("계좌삭제");
			System.out.println("------");
			
			System.out.print("계좌번호: ");
			String number = sc.next();
			int index = findIndex(number);
			if(index >=0) {
				accounts[index] = accounts[count];
				accounts[count] = null;
				count--;
			} else {
				System.out.println("존재하지 않는 계좌번호 입니다."); // 계좌가 존재하지 않을 경우(account == -1) 안내 문구 출력
			}
		}
		

}
