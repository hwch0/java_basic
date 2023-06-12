package ch14.se03.exam01;

import java.awt.Toolkit;

public class BeepPrintExample1 {

	public static void main(String[] args) {
		// 교재 596페이지 예제
		// 0.5초 주기로 beep음과 '띵'프린팅 동시 실행
		// main 스레드는 단일 스레드이기 때문에 코드가 작성된 순서대로 beep음 실행이 완료된 이후 프린팅 시작
		
		// Toolkit 패키지로 beep 5번 실행
		Toolkit toolkit = Toolkit.getDefaultToolkit(); // 싱글톤 객체이므로 new 키워드로 객체 생성 불가
		
		for(int i = 0; i<5; i++) {
			toolkit.beep();
			try {
				Thread.sleep(500); // 0.5초 씩 일시정지 (Interrupted예외 발생)
			} catch (InterruptedException e) {
				// 스레드가 일시정지 상태가 될 경우 Thread의 interrupt() 메서드가 예외를 발생시키기 때문에 반드시 예외처리를 해줘야 함
			}
		}
		
		for(int i = 0; i<5; i++) {
			System.out.println("띵");
			try {
				Thread.sleep(500); // 0.5초 씩 일시정지 (Interrupted예외 발생)
			} catch (InterruptedException e) {
			}
		}
		}
}
