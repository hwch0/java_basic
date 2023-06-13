package ch17.sec10;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

public class OptionalExample {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		
		// 방법1: isPresent() 집계값이 있는지 여부 확인
		OptionalDouble optional = list.stream()
				.mapToInt(Integer :: intValue)
				.average();
		if(optional.isPresent()) {
			System.out.println("방법1_평균: " + optional.getAsDouble());
		} else {
			System.out.println("방법1_평균: 0.0");
		}
		
		// 방법2: orElse() 집계값이 없을 경우 디폴트값 설정
		double avg = list.stream()
				.mapToInt(Integer :: intValue)
				.average()
				.orElse(0.0);
		System.out.println("방법2_평균: " + avg);
		
		// 방법3: ifPresent() 집계값이 있을 경우 Consumer에서 처리
		list.stream()
		.mapToInt(Integer :: intValue)
		.average()
		.ifPresent(a -> System.out.println("방법3_평균: " + a));
		
	}

}
