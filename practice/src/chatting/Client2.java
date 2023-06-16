package chatting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client2 {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TCP 채팅 프로그램 구현 - 클라이언트 (교재 838페이지)
		Socket socket = new Socket("localhost", 50001);
		// Socket 생성과 동시에 연결 요청
		// Socket 객체를 생성할 때 파라미터로 ip주소와 port번호가 들어가면 생성자 함수 내부에서 connect() 함수가 자동으로 실행되면서 연결 요청을 보냄
		System.out.println("[클라이언트2] 연결 성공");
		Scanner scanner = new Scanner(System.in);
		
		while(true) {
			// 데이터 보내기 
			// 방법1.
			// - 연결된 소켓에서 getOuptputStream()으로 출력 스트림 받기
			// - 보낼 메세지에서 바이트 배열 뽑기
			// - 출력 스트림에 .write(바이트 배열) 로 전소이
			String sendMessage = scanner.nextLine();
			if(sendMessage.equals("q")) {
				System.out.println("종료 합니다.");
				break;
			}
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(sendMessage);
			dos.flush();
			System.out.println("[클라이언트2] 데이터 보냄: " + sendMessage);
			
			// 데이터 받기 
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			String receiveMessage = dis.readUTF();
			System.out.println("[클라이언트2] 데이터 받음: " + receiveMessage);
			// ---//
			
		}
		// 연결 끊기
		socket.close();
		System.out.println("[클라이언트2] 연결 끊음");

	}

}
