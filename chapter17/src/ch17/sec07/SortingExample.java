package ch17.sec07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class SortingExample {

	public static void main(String[] args) {
		// Stream 정렬 실습 교재 745페이지
		
		// List 컬렉션 생성
		List<Student> studentList = new ArrayList<>();
		studentList.add(new Student("홍길동", 30));
		studentList.add(new Student("신용권", 10));
		studentList.add(new Student("유미선", 20));
		studentList.add(new Student("신용권", 20));
		studentList.add(new Student("홍길동", 20));
		studentList.add(new Student("홍길자", 20));
		studentList.add(new Student("김유신", 20));
		studentList.add(new Student("유관수", 20));
		
		// 점수를 기준으로 오름차순 정렬한 스트림 (Consumer 익명 구현 객체 사용)
		System.out.println("---------------1번---------------");
		studentList.stream()
					.sorted()
					.forEach(new Consumer<Student>() {
						@Override
						public void accept(Student t) {
							System.out.println(t.getName() + ":" + t.getScore());
						}
					});
		
		System.out.println();
		System.out.println("---------------2번---------------");
		// 점수를 기준으로 내림차순 정렬 스트림 (람다식)
		studentList.stream()
					.sorted(Comparator.reverseOrder())
					.forEach(s -> System.out.println(s.getName() + ":" + s.getScore()));
		
		System.out.println();
		System.out.println("---------------3번---------------");
		// Comparator를 이용한 정렬 (익명 구현 객체)
		studentList.stream()
					.sorted(new Comparator<Student>() {
						@Override
						public int compare(Student o1, Student o2) {
							return o1.getScore() - o2.getScore();
						}
					})
					.forEach(s -> System.out.println(s.getName() + ":" + s.getScore()));
		
		System.out.println();
		System.out.println("---------------4번---------------");
		// Comparator를 이용한 정렬 (람다식)
		studentList.stream()
					.sorted((o1, o2) -> o1.getScore() - o2.getScore())
					.forEach(s -> System.out.println(s.getName() + ":" + s.getScore()));
		
		
		System.out.println();
		System.out.println("---------------5번---------------");
		// (!!과제: 점수를 기준으로 오름차순 정렬 후 이름으로 정렬)
		// 여러 필드로 정렬할 경우 thenComparing 사용 (다중정렬)
		studentList.stream()
					.sorted(Comparator.comparing(Student::getScore).thenComparing(Student::getName))
					.forEach(s -> System.out.println(s.getName() + ":" + s.getScore()));
		
		
		
		
	}

}
