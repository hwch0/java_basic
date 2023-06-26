package Member;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import Server.SocketClient;
import lombok.Data;

@Data
public class Room implements Serializable{
	private static int count =1; // 채팅방 고유 아이디
	private String roomName;
	private String roomId = "";
	Map<String, SocketClient> mapSocketClient = Collections.synchronizedMap(new HashMap<>());
	
	// 채팅방 생성자 
	public Room(String roomName) {
		super();
		this.roomName = roomName;
		this.roomId = String.valueOf(count++);
	}
	
	public void add(String key, SocketClient socketClient) {
		mapSocketClient.put(socketClient.getKey(), socketClient);
	}
	
	public void incoming(SocketClient socketClient, JSONObject jsonObject) {
		
		//채팅방에 입장한다
		socketClient.chatName = jsonObject.getString("data");
		sendToAll(socketClient, "들어오셨습니다.");
		addSocketClient(socketClient);
	}
	
	// 메소드: 채팅방 입장 시 room의 mapSocketClient에 입장한 회원 추가
	public void addSocketClient(SocketClient socketClient) {
	String key = socketClient.getKey();
	mapSocketClient.put(socketClient.getKey(), socketClient);
	System.out.println("입장: " + key);
	System.out.println("현재 채팅 참여자 수: " + mapSocketClient.size() + "\n");
	}
	
	// 메소드: 현재 채팅방에 입장한 모든 회원들에게 메시지 전송
	public void sendToAll(SocketClient sender, String message) {
		// 전송할 메시지 JSON 객체 만들기
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("clientIp", sender.getClientIp());
		jsonObject.put("chatName", sender.getChatName());
		jsonObject.put("message", message);
		String json = jsonObject.toString();
		System.out.println("Room 내부 " + json);
		Collection<SocketClient> socketClients = mapSocketClient.values();
		System.out.println("sender " + sender.getChatName());
		for(SocketClient sc : socketClients) {
			System.out.println("sc " + sc.getChatName());
			if(sc == sender) {
				System.out.println("송신자와 수신자가 같습니다.");
				continue;
			} else {
				sc.sendMessage(json);
			}
		}
	}
	
	// 메소드: 채팅방 나갈 경우 mapSocketClient에서 해당 멤버 삭제
	public void removeSocketClient(SocketClient socketClient) {
		String key = socketClient.getKey();
		mapSocketClient.remove(key);
		sendToAll(socketClient,  socketClient.getChatName()+ "님이 퇴장하셨습니다.");
		System.out.println("퇴장: " + key);
		System.out.println("현재 채팅 참여자 수: " + mapSocketClient.size() + "\n");
	}
	
	// 
	// 메소드: 채팅방에 입장한 모든 멤버의 Socket close
	public void stop() {
		mapSocketClient.values().stream().forEach(sc -> sc.close()); // 채팅방에 연결된 소켓 스트림으로 하나씩 가져와서 종료
		System.out.println("채팅이 종료됩니다.");
	}
	
	public String getKey() {
		return roomId;
	}

}
