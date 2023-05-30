package ch06;

public class BankApplication {

	public static void main(String[] args) {
		boolean isRun = true; // 반복문 제어 변수
		BankAccount bank = new BankAccount();

		while(isRun) {
			bank.printMenu();
			System.out.print("선택 > ");
			int choice = bank.sc.nextInt();
			switch (choice) {
			case 1 -> bank.createAccount();
			case 2 -> bank.printAccounts();
			case 3 -> bank.deposit();
			case 4 -> bank.withdraw();
			case 5 -> bank.deleteAccount();
			case 6 -> {
				isRun = false;
				System.out.println("시스템이 종료 되었습니다.");
			}
			
			}
		}

	}

}
