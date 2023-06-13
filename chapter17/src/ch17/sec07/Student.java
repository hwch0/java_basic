package ch17.sec07;

public class Student implements Comparable<Student>{
	private String name;
	private int score;
	
	
	public Student(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
	public String getName() {return name;}
	public int getScore() {return score;}

	@Override
	public int compareTo(Student o) {
//		return Integer.compare(score, o.score);
		// score와 o.score 같은 경우 0 리턴, 작을 경우 음수 리턴, 클 경우 양수 리턴
		// 함수를 호출하는 시간때문에 상대적으로 수행 시간이 길다
		return score - o.score;
	}

}
