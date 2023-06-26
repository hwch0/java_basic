package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import Member.Member;
import Member.Room;
import lombok.Data;

@Data
public class SocketClient implements Serializable {
	private static final long serialVersionUID = 7061016713042652922L;
	// 필드
	ChatServer chatServer; 
	Socket socket;
	DataInputStream dis; 
	DataOutputStream dos;
	String clientIp;
	public String chatName;
	Map<String, Member> memberList;
	int keyId; 
	static int count = 1; // 로그인 하지 않은 이용자의 고유한 키
	String chatRoomId = ""; // 사용자가 접속한 채팅방 ID
	
	
	public SocketClient(ChatServer chatServer, Socket socket) {
		try {
			this.chatServer = chatServer;
			this.socket = socket;
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
			InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress(); // 소켓에 연결된 클라이언트 정보 가져오기
			this.clientIp = isa.getHostName();
			this.keyId = chatServer.count++;
			this.memberList = chatServer.memberList;
			receive();
			
		} catch(Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	// 대화명과는 별개로 사용자를 구분하는 key값으로 사용
	public String getKey( ) {
		return chatName + "@" + clientIp;
	}
	
	// 서버 수신용 스레드 생성
	// 서버에서 모든 클라이언트들의 메시지를 수신하고 발신한 사람한테만 전송
	public void receive() {
		chatServer.threadPool.execute(() -> {
			// thread 만들 때 익명 객체로 안만드니까 sendToAll에서 this로 객체를 넘길 때 SocketClient 객체가 아니라 Runnable 객체로 넘어감
			// 수신은 끊기지 않고 계속 받아줘야 하니까 while문
			try {
				boolean loop = true;
				while(loop) {
						System.out.println("************SocketClient 실행*********");
						String receiveMessage = dis.readUTF();
						
				        // JSONObject 객체를 생성하여 스트링으로 된 JSON을 파싱합니다.
				        JSONObject jsonObject = new JSONObject(receiveMessage);
						
				        // 클라이언트로 부터 전달 받은 command 추출
						String command = jsonObject.getString("command");
						System.out.println("command : " + command);
						
						switch(command) {
						    case "existUserId":
						    	this.send(makeCommand("existMember", existUserId(jsonObject.getString("uid"))), dos);
						    	loop =false;
						    	break;
							case "saveMember":
								String saveResult = saveMember(jsonObject);
								this.send(makeCommand("saveMember", saveResult), dos);
								loop =false;
								break;
							case "login":
								String loginResult = login(jsonObject);
								this.send(makeCommand("login", loginResult), dos);
								loop =false;
								break;
							case "findPwd" :
								String findPwdResult = findPwd(jsonObject);
								System.out.println("findPwdResult :" + findPwdResult);
								this.send(makeCommand("findPwd", findPwdResult), dos);
								loop =false;
								break;	
							case "getAllMembers" :
								System.out.println("getAllMembers");
								chatServer.readAllmembers();
								this.send(makeCommand("printAllMembers", memberList), dos);
								loop =false;
								break;
							case "updateMember" : 
								updateMember(jsonObject);
								this.send(makeCommand("updateMember", true), dos);
								loop =false;
								break;
							case "deleteMember"	:
								deleteMember(jsonObject);
								this.send(makeCommand("deleteMember", true), dos);
								loop =false;
								break;
							case "createRoom" :
								createRoom(jsonObject, this, dos);
								loop =false;
								break;
							case "printAllRooms" :
								printAllRooms(dos);
								loop =false;
								break;
							case "enterRoom":
								enterRoom(jsonObject);
								loop =false;
								break;
							case "setChatName" : 
								setChatName(jsonObject, dos);
								loop =false;
								break;
							case "incoming":
								incoming(jsonObject);
								break;
							case "message":
								message(jsonObject);
								break;
							case "serverCreateChatRoom":
								serverCreateChatRoom(dos, jsonObject);
								loop = false;
								break;
						}
					}
						
					} catch (IOException e) {
						// 서버에 출력
						System.out.println(getKey() + "님이 나가셨습니다."); 
						
						// 현재 채팅방에 접속한 상태인 경우, 해당 채팅방에서 삭제 처리 및 다른 사용자들에게 메시지 전송
						if(!chatRoomId.equals("")) {
							Room targetRoom = chatServer.chatRooms.get(chatRoomId);
							targetRoom.sendToAll(this, getChatName() + "님이 퇴장하셨습니다.");
							targetRoom.removeSocketClient(this);
						}
					}
		});
	}
	

	private void incoming(JSONObject jsonObject) {
		//채팅방의 아이디를 얻는다 
		chatRoomId = jsonObject.getString("chatRoomId");
		//채팅방의 아이디를 이용하여 채팅방 객체를 얻는다
		Room chatRoom = chatServer.chatRooms.get(chatRoomId);
		
		chatRoom.incoming(this, jsonObject);
	}

	private void serverCreateChatRoom(DataOutputStream dos, JSONObject jsonObject) {
		String roomName = jsonObject.getString("data");
		
		Room chatRoom = chatServer.addChatRoom(roomName);
		
		JSONObject jsonObjectResult = new JSONObject();
		
		try {
			if (chatRoom != null) {
				jsonObjectResult.put("result", true);
				jsonObjectResult.put("chatRoomId", chatRoom.getKey());
			} else {
				jsonObjectResult.put("result", false);
			}
			
			dos.writeUTF(jsonObjectResult.toString());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void incomming(JSONObject jsonObject) {
		System.out.println("소켓 incoming");
		chatRoomId = jsonObject.getString("roomId");
		Room room = chatServer.chatRooms.get(chatRoomId);
		room.incoming(this, jsonObject);
	}

	private void message(JSONObject jsonObject) {
		String message = jsonObject.getString("data");
		chatServer.sendToAll(this, message);
	}

	private void setChatName(JSONObject jsonObject, DataOutputStream dos) {
		chatName = jsonObject.getString("chatName");
		send(makeCommand("chatName", true), dos);
	}

	private void sendToAll(JSONObject jsonObject) {
		System.out.println(chatServer.chatRooms);
		System.out.println("chatRoomId : " + this.chatRoomId);
		Room targetRoom = chatServer.chatRooms.get(chatRoomId);
		if(targetRoom!=null) {
			targetRoom.sendToAll(this, jsonObject.getString("message"));
		} else {
			System.out.println("chatRoomID가 없음");
		}
	}

	private void enterRoom(JSONObject jsonObject) {
		chatName = jsonObject.getString("chatName");
		chatRoomId = jsonObject.getString("roomId");
		System.out.println("chatRoomId : " + chatRoomId);
		Room targetRoom = chatServer.chatRooms.get(chatRoomId);
		// room에 멤버 추가하기
		targetRoom.incoming(this, jsonObject);
//		send(makeCommand("enterRoom", true), dos);
	}

	public Member existMember(String uid) {
		if (memberList.keySet().stream().anyMatch(m -> m.equals(uid))) {
			System.out.println("아이디가 존재합니다\n");
			return memberList.get(uid);
		} else {
			return null;
		}
	}
	
	public boolean existUserId(String uid) {
		if (memberList.keySet().stream().anyMatch(m -> m.equals(uid))) {
			return true;
		} else {
			return false;
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
	
	public void createRoom(JSONObject jsonObject, SocketClient socketClient, DataOutputStream dos) {
		// 입력받은 채팅방 이름으로 채팅방 생성
		String roomName = jsonObject.getString("data");
		Room room = chatServer.addChatRoom(roomName);
		// 생성한 채팅방의 참여자 목록에 채팅방 개설자 추가
		room.addSocketClient(socketClient);
		// 서버의 chatRooms 맵에 생성된 채팅방 추가
//		chatServer.chatRooms.put(room.getRoomId(), room);
		// 참여자의 chatRoomId 설정
//		setChatRoomId(room.getRoomId());
//		System.out.println("setChatRoomId : " + getChatRoomId());
//		// 생성된 room 객체를 JSON 커맨드와 함께 JSON 객체로 만들어 전송
		JSONObject chattingInfo = new JSONObject();
		
		try {
			if(room != null) {
				chattingInfo.put("result", true);
				chattingInfo.put("roomId", room.getRoomId());
				chattingInfo.put("roomName", room.getRoomName());
				
			} else {
				chattingInfo.put("result", false);
			}
			dos.writeUTF(chattingInfo.toString());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printAllRooms(DataOutputStream dos) {
		// 서버의 chatRooms에서 모든 채팅방 목록 가져오기
		Map<String, Room> allRooms = chatServer.chatRooms;
		// 가져온 채팅방 목록을 커맨드와 함께 JSON 객체로 만들어서 전송
		send(makeCommand("printAllRooms", allRooms.values()), dos);
	}
	
	
	// 메소드: JSON 객체 클라이언트로 전달하기
	public void send(JSONObject jsonObject, DataOutputStream dos) {
		System.out.println("json 객체 send");
		try {
			dos.writeUTF(jsonObject.toString());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//메소드: JSON 보내기
	public void send(String json) {
		try {
			dos.writeUTF(json);
			dos.flush();
		} catch(IOException e) {
		}
	}
	
	public void sendMessage(String message) {
		System.out.println("send " + message);
		try {
			dos.writeUTF(message);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//메소드: 연결 종료
	public void close() {
		try { 
			socket.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
