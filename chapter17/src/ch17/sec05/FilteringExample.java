package ch17.sec05;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class FilteringExample {

	public static void main(String[] args) {
		// List 컬레션
		List<String> list = new ArrayList< >();
		list.add("홍길동");
		list.add("신용권");
		list.add("감자바");
		list.add("신용권");
		list.add("신민철");
		
		// 중복 요소 제거
		// Set 컬렉션 활용
		Set<String> set = new HashSet< >();
		for(String name : list) {
			set.add(name);
		}
		System.out.println(set);
		
		// Stream을 활용한 중복 요소 제거
		// distinct 메서드 활용
		list.stream()
			.distinct() // 중간처리 단계 (stream을 계속 사용할 수 있다)
			.forEach(n -> System.out.println(n)); // 최종처리 단계: stream을 끝내겠다 (더 이상 사용할 수 없는 스트림)
		System.out.println();
		
		// 이름이 '신'으로 시작하는 요소만 필터링
		list.stream()
			.filter(n -> n.startsWith("신"))
			.forEach(n -> System.out.println(n));
		System.out.println();
		
		// 
		System.out.println("Predicate 익명 구현 객체");
		list.stream()
		.distinct()
		.filter(new Predicate<String>() {

			@Override
			public boolean test(String t) {
				if (t.startsWith("신")) {
					return true;
				} else {
					return false;
				}
			}
		})
		.forEach(n -> System.out.println(n));
		System.out.println();
		
//		for(String name : list) {
//			if (name.startsWith("신")) {
//				System.out.println(name);
//			}
//		}
		
		

	}

}
