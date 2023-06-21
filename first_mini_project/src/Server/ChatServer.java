package Server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Member.Member;
import Member.Room;

public class ChatServer {
	// 필드
	ServerSocket serverSocket = null;; 
	ExecutorService threadPool = Executors.newFixedThreadPool(100); // 해당 프로그램에 동시에 접속해서 작업할 수 있는 최대 인원
	Map<String, SocketClient> connectedMembers = Collections.synchronizedMap(new HashMap<>()); // 현재 프로그램에 접속한 인원
	Map<String, Member> memberList = Collections.synchronizedMap(new HashMap<>()); // 현재 가입한 인원
	Map<Integer, Room> chatRooms = Collections.synchronizedMap(new HashMap<>()); // 채팅방 목록
	
	// DB (회원목록, 채팅목록)
	final MemberDB memberDB = new MemberDB();
	final String fileName2 = "C:/Users/KOSA/Temp/memberNew.db"; // 회원 목록
	final String fileName3 = "C:/Users/KOSA/Temp/chatRooms.db"; // 채팅방 목록
	File file2 = new File(fileName2);
	File file3 = new File(fileName3);
	ObjectOutputStream oos;
	ObjectInputStream ois;
	
	// 회원목록 hashMap으로 가져오는 함수
//	public Map<String, Member> readAllmembers() {
	public void readAllmembers() {
		
	      if (file2.exists()){
	    	  
	  		try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file2)));
				memberList = (Map<String, Member>) ois.readObject();
				ois.close();
			} catch (IOException|ClassNotFoundException e) {
				System.out.println("readAllmembers()" + " 예외 발생");
				e.printStackTrace();
			} 
	  		System.out.println("-----------------------------------------------------------------");
	  		System.out.println(" NO.|                    MEMBER DATA      			              ");
	  		System.out.println("-----------------------------------------------------------------");
	  		int no = 1;
	  		for(Member m : memberList.values()) {
	  			System.out.println(" " + no +" | "+m);
	  		}
	    	  
	      } else {
	            memberList  = new HashMap<>();
	            System.out.println("회원정보 목록 초기화");
	      }
	}
	
	
	public void readAllrooms() {
		
	      if (file3.exists()){
	  		try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file3)));
				chatRooms = (Map<Integer, Room>) ois.readObject();
				ois.close();
			} catch (IOException|ClassNotFoundException e) {
				System.out.println("readAllrooms()" + " 예외 발생");
				e.printStackTrace();
			} 
	  		System.out.println("-----------------------------------------------------------------");
	  		System.out.println(" NO.|                      CHATTING DATA       	               ");
	  		System.out.println("-----------------------------------------------------------------");
	  		int no = 1;
	  		for(Room r : chatRooms.values()) {
	  			System.out.println(" " + no +" | "+r);
	  		}
	    	  
	      } else {
	            memberList  = new HashMap<>();
	            System.out.println("채팅방 목록 초기화");
	      }
	}
	
	
	// 메소드: 서버 시작
	public void start() throws IOException{
		serverSocket = new ServerSocket(50001);
		System.out.println("[서버] 시작");
		
		Thread thread = new Thread(() -> {
			try {
				while(true) {
				Socket socket;
					socket = serverSocket.accept();
					SocketClient sc = new SocketClient(this, socket);
				}
				} catch (IOException e) {
					System.out.println("서버 start() 오류");
//					e.printStackTrace();
				}
		});
		thread.start();
		readAllmembers();
//		readAllrooms();
	}
	// 메소드: 클라이언트 연결시 connectedMembers 배열에 SocketClient 객체 추가
	public void addSocketClient(SocketClient socketClient) {
		String key = Integer.toString(socketClient.getKeyId()) + "@" + socketClient.getClientIp();
		connectedMembers.put(key, socketClient);
		System.out.println("접속: " + key);
		System.out.println("현재 접속자 수: " + connectedMembers.size() + "\n");
	}
	
	// 메소드: 클라이언트 연결 종료 시 SocketClient 제거
	public void removeSocketClient(Server.SocketClient socketClient) {
		String key = Integer.toString(socketClient.getKeyId()) + "@" + socketClient.getClientIp();
		connectedMembers.remove(key);
		System.out.println("퇴장: " + key);
		System.out.println("현재 접속자 수: " + connectedMembers.size() + "\n");
	}
	
	// 메소드: 모든 클라이언트에게 메시지 보냄 (채팅시에만 사용)
//	public void sendToAll(SocketClient sender, String message) {
//		// 나중에 String message를 json으로 변환
//		// json으로 넘길때 클라이언트 ip, 대화명,메시지 객체로 넘기기
//		Collection<SocketClient> socketClients = connectedMembers.values();
//		for(SocketClient sc : socketClients) {
//			if(sc == sender) continue;
//			sc.send(message);
//		}
//	}
	
	// 메소드: 서버 종료
	public void stop() {
		try {
			serverSocket.close(); 
			threadPool.shutdownNow();// 남아있는 작업과 상관없이 강제 종료
			connectedMembers.values().stream().forEach(sc -> sc.close()); // 채팅방에 연결된 소켓 스트림으로 하나씩 가져와서 종료
			System.out.println("[서버] 종료됨");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws SocketException {
		
		try {
			ChatServer chatServer = new ChatServer();
			chatServer.start();
			
	  		System.out.println("-----------------------------------------------------------------");
			System.out.println("서버를 종료하려면 q를 입력하고 Enter.");
			System.out.println("-----------------------------------------------------------------");
			
			Scanner scanner = new Scanner(System.in);
			while(true) {
				String key = scanner.nextLine();
				if(key.equalsIgnoreCase("q")) break;
			}
			scanner.close(); // Scanner close 하는 이유...
			chatServer.stop();
			
		} catch (IOException e) {
			System.out.println("[서버] " + e.getMessage());
		}
		
	}

}
