import java.io.IOException;

import org.json.JSONObject;

import Client.ChatClient;
import Server.ChatServer;

public class MainView {

	public static void main(String[] args) {
		
		// chatServer 실행
//		ChatServer chatServer = new ChatServer();
//		ChatServer.main(args);
		
		
		// 클라이언트 객체 생성
		ChatClient client1 = new ChatClient();
		client1.connect();
		client1.receive();
		
		

	}

}
