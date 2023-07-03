package member;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDB  {
	// DB 접근과 관련된 CRUD 기능 구현
	// 서버에서 String으로 넘겨주는 데이터를 JSON 혹은 객체 타입으로 변환 후 File로 저장
	// 서버에서 요청하는 데이터를 객체 -> JSON -> String으로 변환하여 전달
	static final String fileName2 = "c:\\Users\\KOSA\\Temp\\memberNew.db";
	static final File file2 = new File(fileName2);
								/*DB 접근용 함수*/
	// 회원정보 리스트를 파일 객체로 저장 (사용)
	public static void saveMemberList(List<Member> memberList) {
		try {
			ObjectOutputStream  oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file2)));
			oos.writeObject(memberList);
			oos.close();
			
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
			memberList = (List<Member>) ois.readObject();

			System.out.println(memberList);

		} catch (Exception e) {
			System.out.println("saveFileMemberList" + " 예외발생");
			e.printStackTrace();
		}
		
		
		
	}

	public static void main(String [] args) {
		List< Member> memberMap = new ArrayList<>();
		for (int i=0;i<10;i++) {
			Member member = new Member("user"+i, "홍길동" + i, "1234", 20+i, "00", "aa", "M", false);
			memberMap.add(member);
		}
		saveMemberList(memberMap);
	}
}
