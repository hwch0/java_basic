package ch15.practice10;

import java.util.TreeSet;

public class TreeSetExample {

	public static void main(String[] args) {
		// chapter15 확인문제 10번
		// TreeSet은 이진트리를 기반으로 한 Set컬렉션 (검색에 특화됨)
		// TreeSet에 객체를 저장하면 자동으로 오름차순 정렬 (객체가 Comparable 인터페이스를 구현하고 있어야 가능)
		// Integer, Double, String 타입은 모두 Comparable을 구현하고 있지만, 사용자 정의 객체는 반드시 Comparable을 구현하고 있어야 함
		// Comparable 인터페이스에는 compareTo() 메소드가 정의되어 있어야 함.
		
		// TreeSet 객체 생성
		TreeSet<Student> treeSet = new TreeSet<Student>();	
		treeSet.add(new Student("blue", 96));
		treeSet.add(new Student("hong", 86));
		treeSet.add(new Student("white", 92));
		
		Student student = treeSet.last();
		System.out.println("최고 점수: " + student.score);
		System.out.println("최고 점수를 받은 아이디: " + student.id);
		
	}

}
