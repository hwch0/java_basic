package chatting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient1 {
	// 필드 
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	String chatName;
	
	// 메소드: 서버 연결
	public void connect() throws UnknownHostException, IOException {
		socket = new Socket("localhost", 50001);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		System.out.println("[클라이언트1] 서버에 연결됨");
	}
	
	// 메소드: 받기 (나중에 Json으로 바꾸기)
	public void receive() {
		Thread thread = new Thread(() -> {
			// 나중에 json으로 바꿔야함 (ip, name, message)
			try {
				while(true) {
					String message;
					message = dis.readUTF();
					System.out.println("수신: " + message);
				}
				
			} catch (IOException e) {
				System.out.println("[클라이언트] 서버 연결 끊김");
				System.exit(0); // 이거 하는 이유가 뭘까....
				
			}
		}); thread.start();
	}
	
	// 메소드: 서버로 보내기 (나중에 Json으로 바꾸기)
	public void send(String message) throws IOException {
		dos.writeUTF(message);
		dos.flush();
	}
	
	// 메소드: 서버 연결 종료
	public void unconnect() throws IOException {
		socket.close();
	}

	// 메소드: 메인
	public static void main(String[] args) {
		try {
			// 클라이언트 객체 생성
			ChatClient1 client1 = new ChatClient1();
			client1.connect();
			
			Scanner scanner = new Scanner(System.in);
			System.out.println("대화명 입력: >>> ");
			client1.chatName = scanner.nextLine();
			client1.send(client1.chatName + "님이 입장하셨습니다.");
			client1.receive(); // 메시지를 수신받는 스레드는 계속 동작
			
			// 클라이언트의 메인 스레드에서는 메시지를 전송하는 작업 수행
			while(true) {
				String message = scanner.nextLine();
				if(message.toLowerCase().equals("q")) {
					System.out.println("채팅을 종료 합니다.");
					break;
				} else {
					// 나중에 채팅 보내는 부분 json으로 변환해서 전송
					client1.send(message);
				}
			}
			
			scanner.close();
			client1.unconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("[클라이언트] 서버 연결 안됨");
		}
		
		
	}

}
