package ch16.sec01;

public class LambdaExample {

	public static void main(String[] args) {
		// 교재 697페이지 람다식 예제
		// 익명 구현 객체로 표현
		action(new Calculable() {
			@Override
			public void calculate(int x, int y) {
				int result = x + y;
				System.out.println("result = " + result + " --익명 구현 객체");
			}
		});
		
		// 람다식 표현
		action((x,y) -> {
			int result = x + y;
			System.out.println("result = " + result + " --람다식 사용");
		});
		
	}

	public static void action(Calculable calculable) {
		// 데이터
		int x = 10;
		int y = 4;
		// 데이터 처리
		calculable.calculate(x, y);
	}
}
