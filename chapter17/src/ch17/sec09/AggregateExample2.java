package ch17.sec09;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class AggregateExample2 {

	public static void main(String[] args) {
		// 스트림이 제공하는 기본 집계 함수 실습 (optional로 바꾸기)
		// 정수 배열
		int [] arr = {1,2,3,4,5};
		
		//Optional 데이터로 받기 
		OptionalDouble avg2 = Arrays.stream(arr)
		.filter(n -> n%2==0)
		.average();
		if(avg2.isPresent()) {
		System.out.println(avg2.getAsDouble());
		} else {
		System.out.println("호출된 값없다");

		}
	
		// 최대값
		OptionalInt max = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.max();
		if(max.isPresent()) {
			System.out.println("2의 배수 중 최대값: " + max.getAsInt());
		} else {
			System.out.println("호출된 값없다");
		}
		
		// 최소값
		 OptionalInt min = Arrays.stream(arr)
				.filter(n -> n%2==0)
				.min();
		 if(min.isPresent()) {
			 System.out.println("2의 배수 중 최소값: " + min.getAsInt());
		 } else {
			 System.out.println("호출된 값없다");
		 }
		
		// 첫번째 요소
		OptionalInt first = Arrays.stream(arr)
				.filter(n -> n%3==0)
				.findFirst();
		if(first.isPresent()) {
			System.out.println("3의 배수 중 첫번째 요소: " + first.getAsInt());
		} else {
			System.out.println("호출된 값없다");
		}
		
		
	}

}
