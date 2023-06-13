package ch17.sec08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LoopingExample {

	public static void main(String[] args) {
		
		// 루핑 실습 (교재 749)
		// 루핑: 스트림에서 요소를 하나씩 반복해서 가져와 처리하는 것
		// peek(): 리턴 타입이 있는 중간처리 메소드
		// forEach(): 리턴 타입이 없는 void형 최종처리 메소드
		
		int[] intArr = {1,2,3,4,5}; 
		
		// 짝수 반복 출력 스트림 (잘못된 처리)
		Arrays.stream(intArr)
		.filter(a -> a%2==0)
		.peek(n -> System.out.println(n)); // 최종처리가 없으므로 동작하지 않음
		
		// peek + 최종처리 
		int total = Arrays.stream(intArr)
		.filter(a -> a%2==0)
		.peek(n -> System.out.println(n))
		.sum();
		System.out.println("총합 : " + total);
		
		// forEach()를 이용해서 반복 처리
		Arrays.stream(intArr)
		.filter(n -> n%2==0)
		.forEach(n -> System.out.println(n));
	}

}
