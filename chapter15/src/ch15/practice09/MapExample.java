package ch15.practice09;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MapExample {

	public static void main(String[] args) {
		// 교재 15단원 확인문제 9번
		// HashMap에서 평균점수, 최고점수, 최고점수를 받은 아이디 출력 코드 작성
		
		// HashMap 객체 생성
		Map<String, Integer> map = new HashMap<>();
		
		// 데이터 추가
		map.put("blue",96);
		map.put("hong",86);
		map.put("white",92);
		
		String name = null; // 최고점을 받은 아이디를 저장하는 변수
		int maxScore = 0; // 최고 점수를 저장하는 변수
		int totalScore = 0; // 점수 합계를 저장하는 변수
		
		// Set 컬렉션의 Iterator(반복자)로 키와 값을 반복해서 얻기
		Set<Entry<String, Integer>> entrySet = map.entrySet();
		Iterator<Entry<String, Integer>> entryIterator = entrySet.iterator();
		
		// hasNext() : 가져올 객체가 있으면 true를 리턴하고 없으면 false를 리턴한다
		while(entryIterator.hasNext()) {
			Entry<String, Integer> entry = entryIterator.next(); // 컬렉션에서 하나의 객체를 가져온다
			
			String k = entry.getKey(); // 현재 객체의 key (아이디)
			Integer v = entry.getValue(); // 현재 객체의 value(점수)
			
			if(v > maxScore) { // 기존의 최고 점수와 현재의 최고 점수를 비교해서 업데이트
				maxScore = v;
				name = k;
			}
			totalScore += v;
			}
		
		System.out.println("평균 점수: " + (int)(totalScore/map.size()));
		System.out.println("최고 점수: " + maxScore);
		System.out.println("최고 점수를 받은 아이디: " + name);
		
	}
	

}
