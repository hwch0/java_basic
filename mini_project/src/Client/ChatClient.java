package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import Member.Member;
import Member.Room;


public class ChatClient {
	
	private final static JSONObject jsonObject = new JSONObject();
	private static String IP_ADDRESS = "localhost";
	private static int PORT_NUMBER = 50001; 

	private Member currentMember = null;
	private boolean incomed = false;
	
	// 필드 
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String chatRoomId = "";
	String chatName;
	String currentId = "";
	
	// 메소드: 서버 연결 및 DataInputSream, DataOutputStream 생성
	public void connect() {
		try {
			socket = new Socket(IP_ADDRESS, PORT_NUMBER);
			dis = new DataInputStream(socket.getInputStream());
			dos = new DataOutputStream(socket.getOutputStream());
			System.out.println("서버에 연결됨");
			
		} catch (IOException e) {
			System.out.println("### 서버연결 오류 ###");
			e.printStackTrace();
		}
	}
	
	// 메소드: 채팅 메시지 받기 
	public void receive() {
		Thread thread = new Thread(() -> {
			try {
				while(incomed) {
					String json = dis.readUTF();
					
					// 서버가 전송한 메시지를 받을 json객체 생성
					JSONObject jsonObject = new JSONObject(json);
					String clientIp = jsonObject.getString("clientIp");
					String chatName = jsonObject.getString("chatName");
					String message = jsonObject.getString("message");
					System.out.println("<" + chatName + "@" + clientIp + "> " + message);
				}
				
			} catch (Exception e) {
				System.out.println("[클라이언트] 채팅방 나감");
//				e.printStackTrace();
			}
		}); thread.start();
	}
	
	// 메소드: JSON 객체 서버로 전달하기 json(명령어 : 데이터)
	public void send(JSONObject jsonObject) {
		String message = jsonObject.toString();
		try {
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	// 메소드: JSON을 String 타입으로 전달
	public void sendMessage(String json) throws IOException {
		dos.writeUTF(json);
		dos.flush();
	}
	
	// 메소드: 서버 연결 종료
	public void unconnect() throws IOException {
		socket.close();
	}
	
	// 1. 회원가입
	public void insertMember(Scanner scanner) {
		System.out.println("[회원가입]");
		
		System.out.print("아이디: ");
		final String uid = scanner.next();
		boolean type;
		
		// 입력받은 아이디가 존재하지 않을 경우 회원가입 진행
		if(existUserId(uid)) {
			System.out.println("아이디가 존재합니다. \n");
		} else {
			if(uid.contains("manager")) {
				type = true;
			} else {
				type = false;
			}
			
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
			JSONObject receiveJsonObject = serverSendCommand(jsonObject);
			
			if(receiveJsonObject.getString("data").equals("complete")) {
				System.out.println("회원가입이 완료되었습니다.");
			} else if(jsonObject.getString("data").equals("fail")) {
				System.out.println("존재하는 사용자 입니다.");
			}
		}
		
	}
	
	// 다른 소켓 통신 진행 중에 추가적으로 서버에 요청을 보내기 때문에 별도 소켓 생성
	// 아이디 존재여부 확인 후 소켓 연결 끊어주기
	private boolean existUserId(String uid) {
		// 서버에 요청할 데이터 JSON으로 만들기
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "existUserId");
		jsonObject.put("uid", uid);
		// 소켓 연결, 데이터 보내기, 데이터 받기, 소켓 연결 종료
		JSONObject receiveJsonObject = serverSendCommand(jsonObject);
		System.out.println("아이디 확인 결과" + receiveJsonObject.getBoolean("data"));
		return receiveJsonObject.getBoolean("data");
	}
	
	// 2. 로그인
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
			
			// 소켓 연결, 데이터 보내기, 데이터 받기, 소켓 연결 종료
			JSONObject receiveJsonObject = serverSendCommand(jsonObject);
			
			if(receiveJsonObject.getString("data").equals("")) {
				System.out.println("존재하지 않는 사용자 입니다.");
				
			} else {
				JSONObject currentMemberJson = new JSONObject(receiveJsonObject.getString("data")) ;
				currentMember = Member.makeMember(currentMemberJson);
				currentId = currentMember.getUid();
				System.out.println("안녕하세요. " + currentId +" 님^^ 로그인 되었습니다!");
			}
		}
	}
	
	// 3. 비밀번호 찾기
	public void findPwd(Scanner scanner) {
		// ID, 이름 입력 받으면 password 알려주기
		System.out.println("[비밀번호 찾기]");
		System.out.print("아이디 >>> ");
		String uid = scanner.next();
		System.out.print("핸드폰 번호 >>> ");
		String phoneNumber = scanner.next();
		
        // 찾을 ID를 서버로 전송하여 가입여부 확인 JSONObject 객체를 생성합니다.
        JSONObject jsonObject = new JSONObject();
        JSONObject memberInfo = new JSONObject();
        memberInfo.put("uid", uid);
        memberInfo.put("phoneNumber", phoneNumber);
        jsonObject.put("command", "findPwd");
        jsonObject.put("data", memberInfo);
        
        JSONObject receiveJsonObject = serverSendCommand(jsonObject);
        
		String pwd = receiveJsonObject.getString("data");
		if(pwd.equals("error")) {
			System.out.println("입력한 정보가 잘못되었습니다.");
		} else if(pwd.equals("not exist")) {
			System.out.println("존재하지 않는 사용자 입니다.");
			
		} else {
			System.out.println("비밀번호는 " + pwd + "입니다.");
			System.out.println("findPwd 함수 종료");
		} 
	}


	// 4. 정보수정
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
					
					// JSONObject 객체를 생성합니다.
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("command", "updateMember");
					jsonObject.put("data", currentMember.makeJSON().toString());
					
					// 서버로 JSON 객체 전송
//					send(jsonObject);
					
					JSONObject receiveJsonObject = serverSendCommand(jsonObject);
					if(receiveJsonObject.getBoolean("data")) {
						System.out.println("회원정보가 수정 되었습니다.");
					} else {
						System.out.println("회원정보 수정중 오류 발생");
					}
					exit = false;
				}
				}
			}
		}
	}
	
	// 4-1. 회원정보 전체 출력
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
	
	
	
	
	// 회원정보 리스트 json -> Map 변환
	public Map<String, Member> jsonToMemberHash(JSONObject jsonObject) {
		
		Map<String, Member> hashMap = new HashMap<>();
		
		for (String key : jsonObject.keySet()) {
			String id = key;
			Member member = Member.makeMember(jsonObject.getJSONObject(key));
			hashMap.put(id, member);
		}
		return hashMap;
	}
	
	// 회원정보 리스트 json -> Map 변환
//	public Map<String, Room> jsonToRoomHash(JSONObject jsonObject) {
//		
//		Map<String, Room> hashMap = new HashMap<>();
//		
//		for (String key : jsonObject.keySet()) {
//			String id = key;
//			Member member = Member.makeMember(jsonObject.getJSONObject(key));
//			hashMap.put(id, member);
//		}
//		return hashMap;
//	}
	
	// 모든 멤버 출력 메소드
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
	
	// 모든 채팅방 출력 메소드
	private void printAllRooms(JSONArray roomList) {
  		System.out.println("모든 채팅방 출력");
  		System.out.println("-----------------------------------------------------------------");
  		System.out.println(" NO.|                      		 DATA      			               ");
  		System.out.println("-----------------------------------------------------------------");
//  		int no = 1;
		for (int i=0;i<roomList.length();i++) {
			JSONObject chatRoomJSON = roomList.getJSONObject(i);
			System.out.println(" " + chatRoomJSON.getString("roomId") + " | " + chatRoomJSON.getString("roomName"));
		}
	}
	
	
	// 서버에 명령어 json 객체를 전달하고 값을 전달 받는 메서드
	public JSONObject serverSendCommand(JSONObject jsonObject) {
		try {
			// 소켓 연결하기
			Socket socket = new Socket(IP_ADDRESS, PORT_NUMBER);
			// 데이터 보내기
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(jsonObject.toString());
			dos.flush();
			
			// 데이터 받기
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			JSONObject receiveJsonObject = new JSONObject(dis.readUTF());
			
			// 소켓 연결 끊기
			socket.close();
			return receiveJsonObject;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject; // 서버에서 받아온 json이 없을 경우 null값 반환
		
	}
	
	// 서버에 명령어 json 객체를 전달하고 값을 전달 받는 메서드
	public JSONObject serverSendCommand2(JSONObject jsonObject) {
		try {
			// 소켓 연결하기
			Socket socket = new Socket(IP_ADDRESS, PORT_NUMBER);
			// 데이터 보내기
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(jsonObject.toString());
			dos.flush();
			
			// 데이터 받기
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			JSONObject receiveJsonObject = new JSONObject(dis.readUTF());
			
			return receiveJsonObject;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject; // 서버에서 받아온 json이 없을 경우 null값 반환
		
	}
	// 5. 채팅방 생성
	private void createChatRoom(Scanner scanner) {
		//1. 생성할 채팅 방이름을 입력 받는다
		System.out.print("생성할 채팅방 이름 ? ");
		final String roomName = scanner.next();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "serverCreateChatRoom");
		jsonObject.put("data", roomName);
		
		//2. 서버에 연결 
		//3. 서버에 생성할 채팅방 이름을 전달한다
		JSONObject jsonObjectReceive = serverSendCommand(jsonObject);
		
		boolean result = jsonObjectReceive.getBoolean("result");
		if (!result) {
			System.out.println(jsonObjectReceive.getString("message"));
		} else {
			//4. 성공이면 생성된 채팅방 아이디를 얻는다
			chatRoomId = jsonObjectReceive.getString("chatRoomId");
			//생성한 채팅방에 입장 할 때는 생성된 채팅방 아이디 사용하여 입장한다
			helper_incoming(scanner);
		}
		
	}
	// 6. 채팅방 입장
	private void incoming(Scanner scanner) {
		
//		getAllRooms();
		
		//채팅방 입장 
		System.out.print("입장할 방번호 입력?");
		chatRoomId = scanner.next();
		
		helper_incoming(scanner);
	}
	
	// 6-1. 채팅방 입장
	private void helper_incoming(Scanner scanner) {
		//채팅방 입장 
		try {
			//1. 입장을 메시지 생성
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("command", "incoming");
			jsonObject.put("chatRoomId", chatRoomId);
			jsonObject.put("data", currentMember.getName());
			
			//2. 서버에 연결
			connect();
			
			//3. 입장 메시지를 서버에 전달 
			sendMessage(jsonObject.toString());
			
			//4. 입장상태 설정 
			incomed = true;
			
			//5. 수신 스레드를 생성한다
			receive();
			
			//6. 채팅 메시지를 입력 받아 서버에 전달한다
			System.out.println("--------------------------------------------------");
			System.out.println("보낼 메시지를 입력하고 Enter");
			System.out.println("채팅를 종료하려면 q를 입력하고 Enter");
			System.out.println("--------------------------------------------------");
			
			jsonObject.put("command", "message");

			while(true) {
				String message = scanner.nextLine();
				if(message.toLowerCase().equals("q")) {
					incomed = false;
					break;
				} else {
					jsonObject.put("data", message);
					sendMessage(jsonObject.toString());
				}
			}
			unconnect();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//	6-1. 채팅방 입장 (입장할 채팅방에 대한 정보 전달)
	private void helper_incomming(Scanner scanner) {
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "enterRoom");
		jsonObject.put("roomId", chatRoomId);
		jsonObject.put("chatName", chatName);
		
		try {
		// 서버로 JSON 객체 전송 및 결과 받아오기
		JSONObject receiveJsonObject = serverSendCommand2(jsonObject);
		
		// 채팅 접속 여부 상태 변경
		incomed = receiveJsonObject.getBoolean("data");
		
		// 수신 스레드 생성
		receive();
		
		// 채팅화면 출력
		System.out.println("--------------------------------------------------");
		System.out.println("보낼 메시지를 입력하고 Enter");
		System.out.println("채팅를 종료하려면 q를 입력하고 Enter");
		System.out.println("--------------------------------------------------");
	
		jsonObject.put("command", "message");
		
		while(true) {
			String message = scanner.nextLine();
			if(message.toLowerCase().equals("q")) {
				incomed = false;
				break;
			} else {
				jsonObject.put("data", message);
				sendMessage(jsonObject.toString());
			}
		}
		unconnect();

	} catch (Exception e) {
		e.printStackTrace();
	}
		
	}
		
	
	//	7. 채팅방 목록 조회
	private void getAllRooms() {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
			
		} else {
			// JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("command", "printAllRooms");
			
			// 서버로 JSON 객체 전송
			send(jsonObject);
			// 서버로 부터 채팅방 목록 가져오기
			JSONObject receiveJsonObject = serverSendCommand(jsonObject);
			JSONArray hashMap = receiveJsonObject.getJSONArray("data");
			
//			Map<String, Member> hashMap = jsonToMemberHash((JSONObject)receiveJsonObject.get("data"));
//			printAllMembers(hashMap);
			printAllRooms(hashMap);
		}
	}
	
	//	8. 회원탈퇴 
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
//				send(jsonObject);
				
				JSONObject jsonReceiveObject = serverSendCommand(jsonObject);
				
				if(jsonReceiveObject.getBoolean("data")) {
					System.out.println("정상적으로 탈퇴 되었습니다.");
					logout();
				} else {
					System.out.println("회원 탈퇴 중 오류 발생");
				} 
				logout();
				
			} else {
//			choiceMenuAfterLogin(currentId);
			}
		}
	}
	//	9. 모든 회원목록 보기
	private void getAllMembers(Scanner scanner) {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
			
		} else if(currentMember.getType()) {
			// JSONObject 객체를 생성합니다.
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("command", "getAllMembers");
			
			// 서버로 JSON 객체 전송
//			send(jsonObject);
			JSONObject receiveJsonObject = serverSendCommand(jsonObject);
			Map<String, Member> hashMap = jsonToMemberHash((JSONObject)receiveJsonObject.get("data"));
			printAllMembers(hashMap);
			
		} else {
			System.out.println("관리자만 접근 가능합니다.");
		}
	}
	
	
	// 10. 로그아웃
	private void logout() {
		if(currentId.equals("")) {
			System.out.println("로그인 후 이용하세요.");
		} else {
			System.out.println("로그아웃 되었습니다.");
			currentMember = null;
			currentId = "";
		}
	}

	// 메소드: 메인
	public static void main(String[] args) {
		try {
			// 클라이언트 객체 생성
			ChatClient client1 = new ChatClient();
			client1.connect();
//			client1.receive(); // 메시지를 수신받는 스레드는 계속 동작
			
			
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
//				case 5 -> client1.createChattingRoom(scanner);
				case 5 -> client1.createChatRoom(scanner);
//				case 6 -> client1.enterChattingRoom(scanner);
				case 6 -> client1.incoming(scanner);
				case 7 -> client1.getAllRooms();
				case 8 -> client1.deleteMember(scanner);
				case 9 -> client1.getAllMembers(scanner);
				case 10 -> client1.logout(); 
				case 11 -> {
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
