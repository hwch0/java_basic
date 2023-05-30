package ch06;

public class Account {
	// 멤버 변수 필드
	private String number;
	private String name;
	private int balance;
	
	// 생성자 계좌번호, 계좌주, 초기입금액을 입력 받아 객체 생성
		public Account(String number, String name, int balance) {
			super();
			this.number = number;
			this.name = name;
			this.balance = balance;
		}
		
		// (2.계좌목록 조회) 계좌 객체를 지정된 서식으로 출력하는 메소드 
		public void printAccount() {
			System.out.printf("%s\t%s\t%d\n", number, name, balance);
		}
		
		// (3.예금) 계좌 객체의 예금액을 증가시키는 메소드
		public void incBalance(int plus) {
			balance += plus;
		}
		// (4.출금) 계좌 객체의 예금액을 감소시키는 메소드
		public void decBalance(int minus) {
			balance -= minus;
		}
		
		// (3,4번 공통) 해당 객체의 계좌번호와 입력된 계좌번호의 일치 여부 확인하여 boolean 값을 반환하는 메서드
		public boolean isAccount(String number) {
			return this.number.equals(number);
		}
	
}
