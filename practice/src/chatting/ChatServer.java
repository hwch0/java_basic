package chatting;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {
	// 필드
	ServerSocket serverSocket; // 클라이언트의 응답을 받아들이는 역할 (accept함수가 호출되면 사용자가 추가됨)
	ExecutorService threadPool = Executors.newFixedThreadPool(100); // 스레드를 관리하는 스레드풀
	Map<String, SocketClient> chatRoom = Collections.synchronizedMap(new HashMap<>()); 
	// socket 연결을 관리하는 배열 필요 (별도 스레드)
	// 일반 맵을 사용하면 동기화 부분에 문제가 생길 수 있음
	// 키: 사용자 이름 / 값: socket
	
	// 메소드: 서버 시작
	public void start() throws IOException {
		// 서버 소켓 생성 및 포트 번호 바인딩
		serverSocket = new ServerSocket(50001);
		System.out.println("[서버] 시작됨");
		// 송수신에 대한 부분은 스레드에서 동작
		// 송수신 과정에서 연결이 끊어질 경우, 맵에 추가된 소켓을 제거해 줘야함
		// 이때 두개의 스레드가 하나의 공유 데이터를 참조하기 때문에 동기화(synchronizedMap) 필요
		Thread thread = new Thread(() -> {
			while(true) {
				try {
					Socket socket = serverSocket.accept();
					// 클라이언트와 연결되면, 해당 클라이언트와의 연결을 관리하는 SocketClient 객체 생성
					// 매개값으로 해당 서버와 소켓 전달
					SocketClient sc = new SocketClient(this, socket); 
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		});
		thread.start();
	}
	
	// 메소드: 클라이언트 연결시 chatRoom 배열에 SocketClient 객체 추가
	public void addSocketClient(SocketClient socketClient) {
		String key = socketClient.chatName + "@" + socketClient.clientIp;
		chatRoom.put(key, socketClient);
		System.out.println("입장: " + key);
		System.out.println("현재 채팅자 수: " + chatRoom.size() + "\n");
		
	}
	
	// 메소드: 클라이언트 연결 종료 시 SocketClient 제거
	public void removeSocketClient(SocketClient socketClient) {
		String key = socketClient.chatName + "@" + socketClient.clientIp;
		chatRoom.remove(key);
		System.out.println("나감: " + key);
		System.out.println("현재 채팅자 수: " + chatRoom.size());
		
	}
	
	// 메소드: 모든 클라이언트에게 메시지 보냄
	public void sendToAll(SocketClient sender, String message) {
		// 나중에 String message를 json으로 변환
		// json으로 넘길때 클라이언트 ip, 대화명,메시지 객체로 넘기기
		Collection<SocketClient> socketClients = chatRoom.values();
		for(SocketClient sc : socketClients) {
			if(sc == sender) continue;
			sc.send(message);
		}
	}
	// 메소드: 서버 종료
	public void stop() {
		try {
			serverSocket.close(); 
			threadPool.shutdownNow();// 남아있는 작업과 상관없이 강제 종료
			chatRoom.values().stream().forEach(sc -> sc.close()); // 채팅방에 연결된 소켓 스트림으로 하나씩 가져와서 종료
			System.out.println("[서버] 종료됨");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		try {
			ChatServer chatServer = new ChatServer();
			chatServer.start();
			
			System.out.println("----------------------------------------------------");
			System.out.println("서버를 종료하려면 q를 입력하고 Enter.");
			System.out.println("----------------------------------------------------");
			
			Scanner scanner = new Scanner(System.in);
			while(true) {
				String key = scanner.nextLine();
				if(key.equals("q")) break;
			}
			scanner.close(); // Scanner close 하는 이유...
			chatServer.stop();
			
		} catch (IOException e) {
			System.out.println("[서버] " + e.getMessage());
		}
	}
}
