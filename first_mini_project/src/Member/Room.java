package Member;

import java.io.Serializable;
import java.util.List;

import Server.SocketClient;
import lombok.Data;

@Data
public class Room implements Serializable{
	private static final long serialVersionUID = 4275789900846015100L;
	
	private String roomName;
	private List<SocketClient> members;
	
	public Room(String roomName, List<SocketClient> members) {
		super();
		this.roomName = roomName;
		this.members = members;
	}
	
	

}
