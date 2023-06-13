package ch17.sec01;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class StreamExample {

	public static void main(String[] args) {
		// Set 컬레션 생성 
		// 데이터
		Set<String> set = new HashSet< >();
		set.add("홍길동");
		set.add("신용권");
		set.add("감자바");
		
		// 향상된 for-loop (제일 빠름)
		System.out.println("향상된 for-loop");
		for(String name : set) {
			System.out.println(name);
		}
		
		// Iterator
		System.out.println("Iterator");
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String name = it.next();
			System.out.println(name);
		}
		
		// Stream
		// 처리루틴을 forEach에 등록, 컬렉션에 있는 요소당 처리 루틴을 계속 호출
		// 향상된 for-loop 보다 더 빠른건 stream을 병렬처리하는 것
		System.out.println("Stream 익명 구현 객체");
		Stream<String> stream1 = set.stream();
		
		// 람다식 사용 전
		stream1.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.println(t);
				
			}
		});
		
		// 람다식 적용 (매개변수) -> 실행문
		System.out.println("Stream 람다식");
		Stream<String> stream2 = set.stream();
		stream2.forEach(name -> System.out.println(name));
 }
}
