package chatting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {
	// ServerSocket 객체 생성 (포트 번호 바인딩)
//	private static ServerSocket serverSocket = new ServerSocket(50001);
	private static ServerSocket serverSocket = null;
	private static List<Socket> socketList = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		// TCP 채팅 프로그램 구현 - 서버 (교재 838페이지)
		// TCP 연결형 프로토콜 (상대방이 연결된 상태에서 데이터를 주고 받음)
		// 데이터가 고정 회선을 통해 전달 (데이터가 순서대로 전달, 데이터 손실이 없음)
		// java.net : 자바에서 TCP 네트워킹을 위해 제공하는 패키지 (ServerSocket, Socket 클래스 제공)
		// ServerSocket: 클라이언트의 연결을 수락 / Socket: 클라이언트에서 연결 요청할때와 클라이언트와 서버 양쪽에서 데이터를 주고 받을 때 사용
		// Post: 운영체제가 관리하는 서버 프로그램의 연결 번호 (서버는 시작할 때 특정 Port 번호에 바인딩)
		// IP 주소: 컴퓨터의 고유한 주소 - 네트워크 어댑터(LAN 카드) 마다 할당
		
		// TCP 서버 시작
		startServer();
		// 키보드 입력 (종료 조건 설정)
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String key = scanner.nextLine();
			if(key.toLowerCase().equals("q")) {
				break;
			}
		}
		scanner.close();
		// TCP 서버 종료 
		stopServer();
	}
	
	// 서버 구동 메서드
	public static void startServer() {
		// 통신용 작업 스레드 생성 
		// 메인스레드 하나에서 작업하면 클라이언트 측 요청이 오기 전까지는 스캐너(키보드 입력) 사용이 불가함
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ServerSocket 생성 및 Port 바인딩
					serverSocket = new ServerSocket(50001);
					System.out.println("[서버] 시작");
					
					// 연결 수락 및 데이터 통신
					while(true) {
						System.out.println("\n[서버] 연결 요청을 기다림\n");
						// 연결 수락
						Socket socket = serverSocket.accept(); 
						socketList.add(socket);
						// accept(): 연결요청 수락. 클라이언트가 연결 요청하기 전까지 블로킹 
						// 클라이언트의 연결 요청이 들어오면 블로킹이 해제되고 통신용 socket 을 리턴한다
						
						// 연결된 클라이언트 정보 얻기
						InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
						System.out.println("[서버] " + isa.getHostName()+ isa.getAddress() + isa.getPort() + "의 연결 요청을 수락함");
						
						// 데이터 받기 (TCP는 데이터를 주고 받을 때 stream을 사용함)
						// DataInputStream과 DataOutputStream은 내부적으로 buffer를 가지고있음
			
						DataInputStream dis = new DataInputStream(socket.getInputStream());
						String message = dis.readUTF();
						
						// 데이터 보내기 
						// 전송할 데이터를 버퍼에 저장 후 flush 메소드로 버퍼를 비울 때 출력 됨
//						DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//						dos.writeUTF(message);
//						dos.flush();
//						System.out.println("[서버] 받은 데이터를 다시 보냄: " + message);
						
						// 여러 클라이언트들에게 메세지 보내기
						for(Socket s : socketList ) {
							if(s.isConnected()) {
								DataOutputStream dos = new DataOutputStream(s.getOutputStream());
								dos.writeUTF(message);
								dos.flush();
								System.out.println("[서버] 받은 데이터를 다시 보냄: " + message);
							}
						}

						
						// 연결 끊기
						socket.close();
						System.out.println("[서버] " + isa.getHostName() + "의 연결을 끊습니다.");
						
					}
					
				} catch (Exception e) {
					System.out.println("[서버] " + e.getMessage());
				}
				
			}
		});
		// 스레드 시작
		thread.start();
	}
	
	public static void stopServer() {
		try {
			// ServerSocket을 닫고 Post 언바인딩
			serverSocket.close();
			System.out.println("[서버] 종료됨");
		} catch (Exception e) {
			System.out.println("[서버] " + e.getMessage());
		}
	}
}
