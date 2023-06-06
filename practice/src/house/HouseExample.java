package house;

public class HouseExample {

	public static void main(String[] args) {
		// 방 만들기 과제 
		// 조건1: 집 안에는 안방과 작은방이 있습니다.
		// 조건2: 모든 방의 문은 동일합니다.
		// 조건3: 방마다 창문은 다릅니다.
		
		// 집 객체 생성
		House myHouse = new House();
		
		// 안방 만들기
		int mainRoomNo = myHouse.makeMainRoom();
		
		// 작은방 만들기
		int subRoomNo = myHouse.makeSubRoom();
		
		// 방 조회
		myHouse.printAllRooms();
		
		// 안방 문 열고 닫기
		myHouse.getRoom(mainRoomNo).openDoor();
		myHouse.getRoom(mainRoomNo).closeDoor();
		
		// 안방 창문 열고 닫기
		myHouse.getRoom(mainRoomNo).openWindow();
		myHouse.getRoom(mainRoomNo).closeWindow();
		
		// 작은방 문 열고 닫기
		myHouse.getRoom(subRoomNo).openDoor();
		myHouse.getRoom(subRoomNo).closeDoor();
		
		// 작은방 창문 열고 닫기
		myHouse.getRoom(subRoomNo).openWindow();
		myHouse.getRoom(subRoomNo).closeWindow();
	}

}
