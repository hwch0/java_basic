package ch17.sec05;

import java.util.ArrayList;
import java.util.List;

public class FilteringExample2 {
	public static void main(String[] args) {
		//List 컬렉션 생성
		List<Student> list = new ArrayList< >();
		list.add(new Student("홍길동",  90));
		list.add(new Student("신용권", 100));
		list.add(new Student("감자바",  95));
		list.add(new Student("신용권", 100));
		list.add(new Student("신민철",  95));
 
		//중복 요소 제거
		list.stream()
			.distinct()
			.forEach(n -> System.out.println(n));
		System.out.println();
//		
		//신으로 시작하는 요소만 필터링
		list.stream()
			.filter(student -> student.getName().startsWith("신"))
			.forEach(n -> System.out.println(n));
		System.out.println();

		//중복 요소를 먼저 제거하고,
		//학생의 이름이 신으로 시작하는 요소만 필터링 한 후 
		//학생의 정보를 출력한다 
		list.stream()
			.distinct()
			.filter(student -> student.getName().startsWith("신"))
			.forEach(student -> System.out.println(student));		
	}

}
