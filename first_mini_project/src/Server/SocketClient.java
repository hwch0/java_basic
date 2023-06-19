package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import Member.Member;
import Member.Room;
import lombok.Data;

@Data
public class SocketClient {
	// 필드
	ChatServer chatServer; 
	Socket socket;
	DataInputStream dis; 
	DataOutputStream dos;
	String clientIp;
	String chatName;
	Map<String, Member> memberList;
	int keyId;
	static int count = 1; // 로그인 하지 않은 이용자의 기본키
	static int roomCnt = 1; // 채팅방 기본키
	
	
	public SocketClient(ChatServer chatServer, Socket socket) {
		try {
			this.chatServer = chatServer;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress(); // 소켓에 연결된 클라이언트 정보 가져오기
			this.clientIp = isa.getHostName();
			this.keyId = count;
			this.memberList = chatServer.memberList;
			count++;
			chatServer.addSocketClient(this); // 프로그램에 접속한 멤버에 추가
			receive();
			
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	// 서버 수신용 스레드 생성
	// 서버에서 모든 클라이언트들의 메시지를 수신하고 발신한 사람한테만 전송
	public void receive() {
		chatServer.threadPool.execute(() -> {
			// thread 만들 때 익명 객체로 안만드니까 sendToAll에서 this로 객체를 넘길 때 SocketClient 객체가 아니라 Runnable 객체로 넘어감
				// 수신은 끊기지 않고 계속 받아줘야 하니까 while문
			try {
				while(true) {
						System.out.println("************SocketClient 실행*********");
						String receiveMessage = dis.readUTF();
						
				        // JSONObject 객체를 생성하여 스트링으로 된 JSON을 파싱합니다.
				        JSONObject jsonObject = new JSONObject(receiveMessage);
						
				        // 클라이언트로 부터 전달 받은 command 추출
						String command = jsonObject.getString("command");
						System.out.println("command : " + command);
						
						switch(command) {
						    case "existMember":
						    	
						    	break;
							case "saveMember":
								String saveResult = saveMember(jsonObject);
								this.send(makeCommand("saveMember", saveResult));
								break;
							case "login":
								String loginResult = login(jsonObject);
								this.send(makeCommand("login", loginResult));
								break;
							case "findPwd" :
								String findPwdResult = findPwd(jsonObject);
								System.out.println("findPwdResult :" + findPwdResult);
								this.send(makeCommand("findPwd", findPwdResult));
								break;	
							case "getAllMembers" :
								System.out.println("getAllMembers");
								chatServer.readAllmembers();
								this.send(makeCommand("printAllMembers", memberList));
								break;
							case "updateMember" : 
								updateMember(jsonObject);
								this.send(makeCommand("updateMember", true));
								break;
							case "deleteMember"	:
								deleteMember(jsonObject);
								this.send(makeCommand("deleteMember", true));
								break;
							case "createRoom" :
								createRoom(jsonObject, this);
								this.send(makeCommand("createRoom", true));
								break;
//							case "대화명":
//								this.chatName = message;
//								chatServer.sendToAll(this, "님이 들어오셨습니다.");
//								chatServer.addSocketClient(this); 
//								break;
//							case "" :
//								chatServer.sendToAll(this, message);
//								break;
						}
					}
						
					} catch (IOException e) {
//						chatServer.sendToAll(this, "나가셨습니다.");
						String key = Integer.toString(keyId) + "@" + clientIp;

						System.out.println(key + "나가셨습니다."); 
						
						chatServer.removeSocketClient(this);
//						e.printStackTrace();
					}
		});
	}
	

	public Member existMember(String uid) {
		
		if (memberList.keySet().stream().anyMatch(m -> m.equals(uid))) {
			System.out.println("아이디가 존재합니다\n");
			return memberList.get(uid);
		} else {
			return null;
		}
	}
	
	public String saveMember(JSONObject jsonObject) {
		JSONObject newMemberJson = (JSONObject) jsonObject.get("data");
		Member newMember = Member.makeMember(newMemberJson);
		if(existMember(newMember.getUid())==null) {
			memberList.put(newMember.getUid(), newMember);
			chatServer.memberDB.saveMemberList(memberList);
			return "complete";
		} else {
			System.out.println("존재하는 아이디 입니다.");
			return "fail";
		}
	}
	
	public String findPwd(JSONObject jsonObject) {
		JSONObject memeberInfo = (JSONObject)jsonObject.get("data");
		System.out.println("id :" + memeberInfo.getString("uid"));
		System.out.println("phone :" + memeberInfo.getString("phoneNumber"));
		
		Member findMember = existMember(memeberInfo.getString("uid"));
		if (findMember != null) {
			if((findMember.getUid().equals(memeberInfo.getString("uid"))) 
					&& (findMember.getPhoneNumber().equals(memeberInfo.getString("phoneNumber"))) ) {
				return findMember.getPwd();
			} else {
				return "error";
			}
		} else {
			return "not exist";
		}
	}
	
	public void deleteMember(JSONObject jsonObject) {
		String targetID = jsonObject.getString("data");
		memberList.remove(targetID);
		chatServer.memberDB.saveMemberList(memberList);
	}
	
	public void updateMember(JSONObject jsonObject) {
		JSONObject updatedInfo = new JSONObject(jsonObject.getString("data"));
		Member updatedMember = Member.makeMember(updatedInfo);
		memberList.put(updatedMember.getUid(), updatedMember);
		chatServer.memberDB.saveMemberList(memberList);
	}
	
	public JSONObject makeCommand(String command, Object data) {
		JSONObject jsonCommand = new JSONObject();
		jsonCommand.put("command", command);
		jsonCommand.put("data", data);
		return jsonCommand;
	}
	
	public String login(JSONObject jsonObject) {
		Member result = chatServer.memberDB.checkMemberInfo((JSONObject)jsonObject.get("data"));
		if(result!=null) {
			JSONObject resultJson = result.makeJSON();
			return resultJson.toString();
		} else {
			return "";
		}
	}
	
	public String createRoom(JSONObject jsonObject, SocketClient socketClient) {
		String roomName = jsonObject.getString("data");
		List<SocketClient> roomLeader = null;
		roomLeader.add(socketClient);
		Room newRoom = new Room(roomName, roomLeader);
		chatServer.memberDB.saveRoomList(roomCnt, newRoom);
		return chatName;
		
	}
	
	
	// 메소드: JSON 객체 클라이언트로 전달하기
	public void send(JSONObject jsonObject) {
		try {
			
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(jsonObject.toString());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public void send(String message) {
//		try {
//			dos.writeUTF(message);
//			dos.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	// 메소드: 연결 종료
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
