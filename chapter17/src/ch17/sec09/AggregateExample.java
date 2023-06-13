package ch17.sec09;

import java.util.Arrays;

public class AggregateExample {

	public static void main(String[] args) {
		// 스트림이 제공하는 기본 집계 함수 실습 (교재 753 페이지)
		// 카운팅, 최대, 최소, 평균, 합계
		// 정수 배열
		int [] arr = {1,2,3,4,5};
		
		// 카운팅
		long count = Arrays.stream(arr)
							.filter(n -> n%2==0)
							.count();
		System.out.println("2의 배수 개수: " + count);
		
		// 총합
		long sum = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.sum();
		System.out.println("2의 배수 총합: " + sum);
		
		// 평균
		double avg = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.average()
				.getAsDouble();
		System.out.println("2의 배수 평균: " + avg);
		
		// 최대값
		int max = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.max()
				.getAsInt(); // 메소드를 사용해서 안전하게 값을 받아오는 역할
		System.out.println("2의 배수 중 최대값: " + max);
		
		// 최소값
		int min = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.min()
				.getAsInt();
		System.out.println("2의 배수 개수: " + min);
		
		// 첫번째 요소
		int first = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.findFirst()
				.getAsInt();
		System.out.println("2의 배수 중 첫번째 요소: " + first);
		
		
	}

}
