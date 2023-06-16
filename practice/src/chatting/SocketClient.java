package chatting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class SocketClient {
	// 필드
	ChatServer chatServer; // 필드로 왜 서버를...? -> 서버에서 생성한 스레드풀 가져오려고~
	Socket socket;
	DataInputStream dis; // 기본형 타입의 데이터를 읽고 쓸수 있게 해주는 (변환 작업이 필요 없는) DataStream 하지만 파일 클래스와 직접 연동할 수 없기 때문에 파일에 읽고 쓸 때는 FileInputStream FileOutputStream을 매개값으로 Data입출력 스트림을 생성해 줘야 함
	DataOutputStream dos;
	String clientIp;
	String chatName;
	
	// 생성자
	public SocketClient(ChatServer chatServer, Socket socket) {
		try {
			this.chatServer = chatServer;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress(); // 소켓에 연결된 클라이언트 정보 가져오기
			this.clientIp = isa.getHostName();
			receive();
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	// 서버 수신용 스레드 생성 (나중에 json으로)
	// 서버에서 모든 클라이언트들의 메시지를 수신하고 모든 사용자들에게 재전송
	public void receive() {
		chatServer.threadPool.execute(() -> {
			// thread 만들 때 익명 객체로 안만드니까 sendToAll에서 this로 객체를 넘길 때 SocketClient 객체가 아니라 Runnable 객체로 넘어감
				// 수신은 끊기지 않고 계속 받아줘야 하니까 while문
			try {
				while(true) {
						String receiveMessage = dis.readUTF();
						String[] messageList = receiveMessage.split(":");
						String command = messageList[0];
						String message = messageList[1];
						
						switch(command) {
							case "대화명":
								this.chatName = message;
								chatServer.sendToAll(this,this.chatName +  "님이 들어오셨습니다.");
								chatServer.addSocketClient(this); 
								break;
							case "" :
								chatServer.sendToAll(this, message);
								break;
						}
						
				}
					} catch (IOException e) {
						chatServer.sendToAll(this, "나가셨습니다.");
						chatServer.removeSocketClient(this);
//						e.printStackTrace();
					}
		});
	}
	// 메소드: String message 보내기 -- 어디로 보내는 send일까...
	public void send(String message) {
		try {
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 메소드: 연결 종료
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
