package ch14.practiceMakeThread;

class subThread extends Thread {
	@Override
	public void run() {
		for(int i=0 ; i<10 ; i++) {
			System.out.println("Thread-3");
			try {Thread.sleep(500);} catch (InterruptedException e) {
			}
		}
	}
}

public class MakeThread {

	public static void main(String[] args) throws InterruptedException {
		// 스레드 생성 연습
		// 스레드를 4가지 방법으로 생성 후 각 스레드의 이름을 출력해보고 결과 확인
		// 첫번째 join 없이 실행 (메인스레드가 먼저 종료됨)
		// 두번째 join 추가 (모든 작업 스레드가 수행 완료된 이후에 메인스레드 종료)
		
		// 1. Thread 클래스로 직접 생성 (Runnable 구현 객체를 매개값으로 갖는 생성자 호출)
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				for(int i=0 ; i<10 ; i++) {
					System.out.println("Thread-1");
					try {Thread.sleep(500);} catch (InterruptedException e) {
					}
				}
			}
		};
		
		Thread thread1 = new Thread(runnable);
		
		// 2. Thread 클래스로 직접 생성 시 Runnable 익명 구현체 사용
		
		Thread thread2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0 ; i<10 ; i++) {
					System.out.println("Thread-2");
					try {Thread.sleep(500);} catch (InterruptedException e) {
					}
				}
			}
		});
		
		// 3. Thread 클래스를 상속받은 자식 클래스로 객체 생성
		Thread thread3 = new subThread();
		
		
		// 4. Thread 클래스를 상속받은 익명 자식 객체 사용
		Thread thread4 = new Thread() {
			@Override
			public void run() {
				for(int i=0 ; i<10 ; i++) {
					System.out.println("Thread-4");
					try {Thread.sleep(500);} catch (InterruptedException e) {
					}
				}
			}
		};
		
		
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		// join 메소드를 호출한 메인메소드는 일시 정지 상태가 되고, 모든 작업스레드가 종료된 이후 다시 실행 대기 모드 상태로 돌아간다
		thread1.join();
		thread2.join();
		thread3.join();
		thread4.join();
		
		System.out.println("메인스레드 종료");
		
	}

}
