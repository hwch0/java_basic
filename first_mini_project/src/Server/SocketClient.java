package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import chatting.Member;
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
	static int count = 1;
	
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
	// 서버 수신용 스레드 생성 (나중에 json으로)
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
						
						
						String command = jsonObject.getString("command");
//						String message = receiveMessage.split(":")[1];
						System.out.println("command : " + command);
						
						switch(command) {
						    case "existMember":
						    	
						    	break;
							case "saveMember":
								if(saveMember(jsonObject)) {
									this.send(makeCommand("saveMember", "complete"));
								} else {
									this.send(makeCommand("saveMember", "fail"));
								}
								
//								JSONObject newMemberJson = (JSONObject) jsonObject.get("data");
//								Member newMember = Member.makeMember(newMemberJson);
//								memberList.put(newMember.getUid(), newMember);
//								chatServer.memberDB.saveMemberList(memberList);
//								chatServer.memberDB.saveMember((JSONObject)jsonObject.get("data"));
//								saveMemberList(jsonObject.getString("data"));
								break;
							case "checkMemberInfo":
								Member result = chatServer.memberDB.checkMemberInfo((JSONObject)jsonObject.get("data"));
								JSONObject loginResult = new JSONObject();
								loginResult.put("command", "login");
								
								if(result!=null) {
									loginResult.put("data", result.getUid());
								} else {
									loginResult.put("data", "");
								}
//								System.out.println("member json 변환" + result.makeJSON().toString());
								System.out.println("socketClient - checkMemberInfo : 결과 JSON 객체 확인" + loginResult.toString());
								this.send(loginResult);
								break;
							case "findPwd" :
								
								String findPwdResult = findPwd(jsonObject);
								System.out.println("findPwdResult :" + findPwdResult);
								this.send(makeCommand("findPwd", findPwdResult));
								
//								JSONObject memeberInfo = (JSONObject)jsonObject.get("data");
//								System.out.println("id :" + memeberInfo.getString("uid"));
//								System.out.println("phone :" + memeberInfo.getString("phoneNumber"));
//								
//								JSONObject findPwdResult = new JSONObject();
//								findPwdResult.put("command", "findPwd");
//
//								Member findMember = existMember(memeberInfo.getString("uid"));
//								System.out.println("findMember" + findMember.toString());
//								if (findMember != null) {
//									if((findMember.getUid().equals(memeberInfo.getString("uid"))) 
//											&& (findMember.getPhoneNumber().equals(memeberInfo.getString("phoneNumber"))) ) {
//										findPwdResult.put("data", findMember.getPwd());
//									} else {
//										findPwdResult.put("data", "error");
//									}
//								} else {
//									findPwdResult.put("data", "not exist");
//								}
//								
//								this.send(findPwdResult);
								
//								String findPassword = chatServer.memberDB.findPassword((JSONObject)jsonObject.get("data"));
//								JSONObject findPwdResult = new JSONObject();
//								findPwdResult.put("command", "findPwd");
//								findPwdResult.put("data", findPassword);
//								this.send(findPwdResult);
								break;	
							case "getAllMembers" :
								System.out.println("getAllMembers");
								chatServer.readAllmembers();
								this.send(makeCommand("printAllMembers", memberList));
								
								
//								JSONObject memberMapResult = new JSONObject();
//								memberMapResult.put("command", "printAllMembers");
//								memberMapResult.put("data", memberList);
//								this.send(memberMapResult);
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
	
	
	// DB CRUD 관련 기능
	// 1. 회원목록 저장 (회원리스트를 JSON object로 받아서 DB로 저장)
	public void saveMemberList(String message) {
		
        // JSONObject 객체를 생성하여 스트링으로 된 JSON을 파싱합니다.
        JSONObject jsonObject = new JSONObject(message);
        
        
        // Json을 다시 Map으로 변환해서 파일로 저장해야하는데 우선 JSON을 바로 파일로 저장하겠음

//        // 결과를 저장할 Map 객체를 생성합니다.
//        Map<String, Member> map = new HashMap<>();
//
//        // JSONObject에서 key를 가져와서 각각의 Member 객체를 생성하여 Map에 저장합니다.
//        for (String key : jsonObject.keySet()) {
//            JSONObject memberJson = jsonObject.getJSONObject(key);
//            Member member = new Member(memberJson.getString("name"), memberJson.getInt("age"));
//            map.put(key, member);
//        }
        
	}
	
	public Member existMember(String uid) {
		
		if (memberList.keySet().stream().anyMatch(m -> m.equals(uid))) {
			System.out.println("아이디가 존재합니다\n");
			return memberList.get(uid);
		} else {
			return null;
		}
	}
	
	public boolean saveMember(JSONObject jsonObject) {
		JSONObject newMemberJson = (JSONObject) jsonObject.get("data");
		Member newMember = Member.makeMember(newMemberJson);
		if(existMember(newMember.getUid())==null) {
			memberList.put(newMember.getUid(), newMember);
			chatServer.memberDB.saveMemberList(memberList);
			return true;
		} else {
			System.out.println("존재하는 아이디 입니다.");
			return false;
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
	
	public JSONObject makeCommand(String command, Object data) {
		JSONObject jsonCommand = new JSONObject();
		jsonCommand.put("command", command);
		jsonCommand.put("data", data);
		return jsonCommand;
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
