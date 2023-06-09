package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONObject;

import Member.Member;
import Member.Room;
import chatting.MemberManager;


public class ChatClient {

	// 필드 
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String chatName;
	String currentId = "";
	Member currentMember;
	
	// 메소드: 서버 연결 및 메인 메뉴 실행
	public void connect() {
		try {
			socket = new Socket("192.168.0.147", 50001);
//			socket = new Socket("localhost", 50001);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			System.out.println("[클라이언트1] 서버에 연결됨");
			
		} catch (IOException e) {
			System.out.println("### 서버연결 오류 ###");
			e.printStackTrace();
		}
//		this.receive();
//		mm.setConnectedMember(this);
//		mm.choiceMenu();
		
	}
	
	// 메소드: 받기 
	public void receive() {
		System.out.println("receive 함수 실행");
		Thread thread = new Thread(() -> {
			// 나중에 json으로 바꿔야함 (ip, name, message)
			boolean flag = true;
			try {
				while(flag) {
					String message = dis.readUTF();
					
					// 서버가 전송한 메시지를 받을 json객체 생성
					JSONObject jsonObject = new JSONObject(message);
					String command = jsonObject.getString("command");
					System.out.println("command: " + command);
					switch (command) {
					
					case "saveMember" -> {
						if(jsonObject.getString("data").equals("complete")) {
							System.out.println("회원가입이 완료되었습니다.");
						} else if(jsonObject.getString("data").equals("fail")) {
							System.out.println("존재하는 사용자 입니다.");
						}
					}
					case "login" -> {
						
						if(jsonObject.getString("data").equals("")) {
							System.out.println("존재하지 않는 사용자 입니다.");
							
						} else {
							JSONObject currentMemberJson = new JSONObject(jsonObject.getString("data")) ;
//							System.out.println("currentMemberJson" + currentMemberJson.toString());
							currentMember = Member.makeMember(currentMemberJson);
							currentId = currentMember.getUid();
							System.out.println("안녕하세요. " + currentId +" 님^^ 로그인 되었습니다!");
						}
					}
					case "findPwd" -> {
						System.out.println("findPwd 함수 실행");
						String pwd = jsonObject.getString("data");
						if(pwd.equals("error")) {
							System.out.println("입력한 정보가 잘못되었습니다.");
						} else if(pwd.equals("not exist")) {
							System.out.println("존재하지 않는 사용자 입니다.");
							
						} else {
							System.out.println("비밀번호는 " + pwd + "입니다.");
							System.out.println("findPwd 함수 종료");
						} 
					}
					case "deleteMember" -> {
						System.out.println("deleteMember 함수 실행");
						if(jsonObject.getBoolean("data")) {
							System.out.println("정상적으로 탈퇴 되었습니다.");
							logout();
						} else {
							System.out.println("회원 탈퇴 중 오류 발생");
						} 

					}
					case "updateMember" -> {
						System.out.println("updateMember 함수 실행");
						if(jsonObject.getBoolean("data")) {
							System.out.println("회원정보가 수정 되었습니다.");
						} else {
							System.out.println("회원정보 수정중 오류 발생");
						}
//						jsonObject.clear();
					}
					
					case "printAllMembers" -> {
						System.out.println("전체 회원 조회");
//						System.out.println(jsonObject.get("data"));
						Map<String, Member> hashMap = jsonToHash((JSONObject)jsonObject.get("data"));
						printAllMembers(hashMap);
//						System.out.println("hashMap"  +hashMap.toString());
					}
					case "createRoom" -> {
						System.out.println("createRoom 함수 실행");
						
//						List<String> chatInfo = jsonObject.getJSONArray("data").get;
						System.out.println("roomName" + jsonObject.getJSONArray("data").getString(0));
//						System.out.println("roomPort" + jsonObject.getJSONArray("data").getString(1));
						
					} 
					case "printAll" -> {
						System.out.println("printAll 함수 실행");
						Map<Integer, Room> allRooms = (Map<Integer, Room>) jsonObject.get("data");
						System.out.println(allRooms.toString());
					}
					}
					
				}
				
			} catch (IOException e) {
				System.out.println("[클라이언트] 서버 연결 끊김");
				System.exit(0); // 이거 하는 이유가 뭘까....
				
			}
		}); thread.start();
	}
	
	// 메소드: JSON 객체 서버로 전달하기 json(명령어 : 데이터)
	public void send(JSONObject jsonObject) {
		String message = jsonObject.toString();
		try {
			
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// 메소드: 서버 연결 종료
	public void unconnect() throws IOException {
		socket.close();
	}
	
	public void insertMember(Scanner scanner) {
		System.out.println("[회원가입]");
		
		System.out.print("아이디: ");
		final String uid = scanner.next();
		boolean type;
		if(uid.contains("manager")) {
			type = true;
		} else {
			type = false;
		}
		
		// 입력받은 아이디가 존재하는지 확인하는 코드 필요
		System.out.print("이름: ");
		final String name = scanner.next();
		
		System.out.print("비밀번호: ");
		final String pwd = scanner.next();
		System.out.print("나이: ");
		final int age = scanner.nextInt();
		System.out.print("전화번호: ");
		final String phoneNumber = scanner.next();
		System.out.print("주소: ");
		final String address = scanner.next();
		System.out.print("성별: ");
		final String gender = scanner.next();
		
		Member newMember = new Member(uid, name, pwd, age, phoneNumber, address, gender, type);
		System.out.println(newMember.toString());
		System.out.println();
		
        // JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", "saveMember");
        jsonObject.put("data", newMember.makeJSON());
        System.out.println("insertMember json 변환 확인 : " + jsonObject.toString());
		
        // 서버로 JSON 객체 전송
        send(jsonObject);
	}
	
	public void login(Scanner scanner) {
		if (!currentId.equals("")) {
			System.out.println("현재 로그인된 상태 입니다. 로그인 아이디: " + currentId);
		} else {
			System.out.println("[로그인]");
			System.out.print("아이디 >>> ");
			String uid = scanner.next();
			System.out.print("비밀번호 >>> ");
			String pwd = scanner.next();
			
			// 찾을 ID를 서버로 전송하여 가입여부 확인 JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			JSONObject memberInfo = new JSONObject();
			memberInfo.put("uid", uid);
			memberInfo.put("pwd", pwd);
			jsonObject.put("command", "login");
			jsonObject.put("data", memberInfo);
			
			System.out.println("checkMemberInfo json 변환 확인 : " + jsonObject.toString());
			
			// 서버로 JSON 객체 전송
			send(jsonObject);
		}
		

	}
	
	public void findPwd(Scanner scanner) {
		// Id, 이름 입력 받으면 password 알려주기
		System.out.println("[비밀번호 찾기]");
		System.out.println("아이디 >>> ");
		String uid = scanner.next();
		System.out.println("핸드폰 번호 >>> ");
		String phoneNumber = scanner.next();
		
        // 찾을 ID를 서버로 전송하여 가입여부 확인 JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        JSONObject memberInfo = new JSONObject();
        memberInfo.put("uid", uid);
        memberInfo.put("phoneNumber", phoneNumber);
        jsonObject.put("command", "findPwd");
        jsonObject.put("data", memberInfo);
		
        // 서버로 JSON 객체 전송
        send(jsonObject);
	}
	
	private void logout() {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
		} else {
			System.out.println("로그아웃 되었습니다.");
			currentMember = null;
			currentId = "";
		}
	}
	
	public void memberInfo() {
			System.out.println("1. 아이디 : " + currentMember.getUid());
			System.out.println("2. 이름 : " + currentMember.getName());
			System.out.println("3. 비밀번호 : " + currentMember.getPwd());
			System.out.println("4. 나이 : " + currentMember.getAge());
			System.out.println("5. 전화번호 : " + currentMember.getPhoneNumber());
			System.out.println("6. 주소 : " + currentMember.getAddress());
			System.out.println("7. 성별 : " + currentMember.getGender());
			
			System.out.println("수정을 원하시는 항목의 번호를 입력해주세요. (1~7번)");
			System.out.println("저장 후 종료를 원하실 경우 8번을 눌러주세요 >>> ");
	}
	
	private void updateInfo(Scanner scanner) {
		System.out.println("currentId" + currentId);
		if(currentId.equals("")) {
			System.out.println("로그인 하세요");
		} else {
			boolean exit = true;
			while(exit) {
				System.out.println("[회원정보 수정]");
				// 현재 정보 출력
				memberInfo();
				// 수정하고 싶은 항목 번호 입력
				int num = scanner.nextInt();
				switch (num) {
				case 1 -> {
					System.out.println("아이디는 수정 불가합니다!!");
				}
				case 2 -> {
					System.out.println("이름 수정 >>> ");
					String newinfo = scanner.next();
					currentMember.setName(newinfo);
				}
				case 3 -> {
					System.out.println("비밀번호 수정 >>> ");
					String newinfo = scanner.next();
					currentMember.setPwd(newinfo);
				}
				case 4 -> {
					System.out.println("나이 수정 >>> ");
					int newinfo = scanner.nextInt();
					currentMember.setAge(newinfo);
				}
				case 5 -> {
					System.out.println("전화번호 수정 >>> ");
					String newinfo = scanner.next();
					currentMember.setPhoneNumber(newinfo);
				}
				case 6 -> {
					System.out.println("주소 수정 >>> ");
					String newinfo = scanner.next();
					currentMember.setAddress(newinfo);
				}
				case 7 -> {
					System.out.println("성별 수정 >>> ");
					String newinfo = scanner.next();
					currentMember.setGender(newinfo);
				}
				
				case 8 -> {
					
					// memberMap json으로 변환
					// JSONObject 객체를 생성합니다.
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("command", "updateMember");
					jsonObject.put("data", currentMember.makeJSON().toString());
					
					// 서버로 JSON 객체 전송
					send(jsonObject);
					
					System.out.println("수정된 정보가 DB에 반영되었습니다.");
					exit = false;
				}
				}
			}
		}
		
	}
	
	private void enterChattingRoom(Scanner scanner) {
		// JSONObject 객체를 생성합니다.
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "printAll");
		
		// 서버로 JSON 객체 전송
		send(jsonObject);
		
		// 서버가 전송한 메시지를 받을 json객체 생성
		String message;
//		try {
//			message = dis.readUTF();
//			JSONObject jsonObject1 = new JSONObject(message);
//			Map<Integer, Room> allRooms = (Map<Integer, Room>) jsonObject1.get("data");
//			System.out.println(allRooms.toString());
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		System.out.println("[채팅방 입장하기]");
		System.out.print("입장하고 싶은 채팅방 번호를 입력해주세요 >>>");
		String roomPort = scanner.next();
	}

	private void createChattingRoom(Scanner scanner) {
		System.out.println("currentId" + currentId);
		if(currentId.equals("")) {
			System.out.println("로그인 하세요");
		} else {
			System.out.println("[채팅방 만들기]");
			System.out.print("채팅방 이름을 입력해주세요 >>>");
			String roomName = scanner.next();
//			System.out.print("채팅방 포트번호 >>>");
//			String portNum = scanner.next();
			
			// JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("command", "createRoom");
			jsonObject.put("data", roomName);
//			jsonObject.put("port", portNum);
			
			// 서버로 JSON 객체 전송
			send(jsonObject);
			}
	}
	
	private void deleteMember(Scanner scanner) {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
		} else {
			System.out.println("[회원탈퇴]");
			System.out.println("정말로 탈퇴 하시겠습니까? (y/n)");
			
			String answer = scanner.next();
			if(answer.equals("y")) {
				
				// JSONObject 객체를 생성합니다.
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("command", "deleteMember");
				jsonObject.put("data", currentMember.getUid());
				
				// 서버로 JSON 객체 전송
				send(jsonObject);
				logout();
			} else {
//			choiceMenuAfterLogin(currentId);
			}
		}
	}
	
	private void getAllMembers(Scanner scanner) {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
		} else if(currentMember.getType()) {
			// memberMap json으로 변환
			// JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("command", "getAllMembers");
			
			// 서버로 JSON 객체 전송
			send(jsonObject);
		} else {
			System.out.println("관리자만 접근 가능합니다.");
		}
	}
	// 회원정보 리스트 json -> Map 변환
	public Map<String, Member> jsonToHash(JSONObject jsonObject) {
		
		Map<String, Member> hashMap = new HashMap<>();
		
		for (String key : jsonObject.keySet()) {
			String id = key;
			Member member = Member.makeMember(jsonObject.getJSONObject(key));
			hashMap.put(id, member);
		}
		return hashMap;
	}
	
	private void printAllMembers(Map<String, Member> memberList) {
  		System.out.println("모든 멤버 출력");
  		System.out.println("-----------------------------------------------------------------");
  		System.out.println(" NO.|                      		 DATA      			               ");
  		System.out.println("-----------------------------------------------------------------");
  		int no = 1;
  		for(Member m : memberList.values()) {
  			System.out.println(" " + no +" | "+m);
  			no++;
  		}
	}
	

	// 메소드: 메인
	public static void main(String[] args) {
		try {
			// 클라이언트 객체 생성
			ChatClient client1 = new ChatClient();
			client1.connect();
			client1.receive(); // 메시지를 수신받는 스레드는 계속 동작
			
			
			Scanner scanner = new Scanner(System.in);
			boolean exit = true;
			
			while(exit) {
				Display.displayMenu();
				int num = scanner.nextInt();
				switch (num) {
				case 1 -> client1.insertMember(scanner); 
				case 2 -> client1.login(scanner);
				case 3 -> client1.findPwd(scanner);
				case 4 -> client1.updateInfo(scanner); 
				case 5 -> client1.createChattingRoom(scanner);
				case 6 -> client1.enterChattingRoom(scanner);
				case 7 -> client1.deleteMember(scanner);
				case 8 -> client1.getAllMembers(scanner);
				case 9 -> client1.logout(); 
				case 10 -> {
					try {
						client1.unconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
					exit = false;
				}
				}
			} System.out.println("프로그램이 종료 되었습니다.");
			
			
			scanner.close();
			client1.unconnect();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[클라이언트] 서버 연결 안됨");
		}
		
	}

	


}
