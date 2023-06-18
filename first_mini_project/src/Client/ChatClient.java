package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONObject;

import Member.MemberManager;
import chatting.Member;



public class ChatClient {

	// 필드 
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String chatName;
	
	
	
	
	MemberManager mm = new MemberManager(this); // 프로그램 화면 및 멤버관리 메소드 실행

	
	
	// 메소드: 서버 연결 및 메인 메뉴 실행
	public void connect() {
		try {
			socket = new Socket("localhost", 50001);
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
	
	// 메소드: 받기 (나중에 Json으로 바꾸기)
	public void receive() {
		System.out.println("receive 함수 실행");
		Thread thread = new Thread(() -> {
			// 나중에 json으로 바꿔야함 (ip, name, message)
			try {
				while(true) {
					String message;
					message = dis.readUTF();
					System.out.println("수신: " + message);
					
					// 서버가 전송한 메시지를 받을 json객체 생성
					JSONObject jsonObject = new JSONObject(message);
					
					String command = jsonObject.getString("command");
					System.out.println("수신:" + command);
					
					switch (command) {
					case "login" -> {
						System.out.println("login 함수 실행");
						Member member = (Member) jsonObject.get("data");
						mm.setCurrentMember(member);
						System.out.println("login 함수 종료");
					}
					case "findPwd" -> {
						System.out.println("findPwd 함수 실행");
						String pwd = jsonObject.getString("data");
						System.out.println("비밀번호는 " + pwd + "입니다.");
						System.out.println("findPwd 함수 종료");
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

	// 메소드: 메인
	public static void main(String[] args) {
		try {
			// 클라이언트 객체 생성
			ChatClient client1 = new ChatClient();
			client1.connect();
			client1.receive(); // 메시지를 수신받는 스레드는 계속 동작
			
			client1.mm.choiceMenu();
			
//			Scanner scanner = new Scanner(System.in);
//			System.out.println("대화명 입력: >>> ");
//			client1.chatName = scanner.nextLine();
//			client1.send(client1.chatName + "님이 입장하셨습니다.");
			
			// 클라이언트의 메인 스레드에서는 메시지를 전송하는 작업 수행
//			while(true) {
//				String message = scanner.nextLine();
//				if(message.toLowerCase().equals("q")) {
//					System.out.println("채팅을 종료 합니다.");
//					break;
//				} else {
//					// 나중에 채팅 보내는 부분 json으로 변환해서 전송
//					client1.send(message);
//				}
//			}
			
//			scanner.close();
			client1.unconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[클라이언트] 서버 연결 안됨");
		}
		
		
	}


}
