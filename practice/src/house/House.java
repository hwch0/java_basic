package house;

import java.util.ArrayList;

public class House {
	
	// 멤버 인스턴스 추상 클래스
	abstract class Room {
		// 문은 방마다 동일
		void openDoor() {
			System.out.println("방 문을 엽니다.");
		}
		void closeDoor() {
			System.out.println("방 문을 닫습니다.");
		}
		// 창문은 방마다 다름 (객체 생성 시 재정의 필요)
		abstract void openWindow();
		abstract void closeWindow();
		
	}
	// 생성된 방을 대입할 변수
	private Room room;
	
	// 생성된 방의 개수 (인덱스로 사용)
	private int roomNo = 0;

	// 집 안에 방 객체를 저장할 배열을 멤버 필드로 선언
	private ArrayList<Room> roomList = new ArrayList<>();
	
	
	// roomNo로 방 객체 가져오기
	public Room getRoom(int roomNo) {
		return roomList.get(roomNo);
	}
	
	// 집안에 생성된 모든 방 출력
	public void printAllRooms() {
		if(this.roomList.size() != 0) {
			for(Room room : roomList) {
				System.out.println(room.toString());
			}
		} else {
			System.out.println("생성된 방이 없습니다.");
		}
	}
	
	
	// 안방 만들고 방번호 return
	// 방 객체를 만들면서 창문 메서드 재정의
	public int makeMainRoom() {
		room = new Room() {
			@Override
			void openWindow() {
				System.out.println("안방 창문을 엽니다.");
			}
			@Override
			void closeWindow() {
				System.out.println("안방 창문을 닫습니다.");
			}
			
		};
		roomList.add(room);
		roomNo++;
		System.out.println("안방을 만들었습니다. 현재 방의 개수 : " + roomNo);
		
		return roomNo-1;
		
	}
	
	
	public Room makeMainRoom2() {
		room = new Room() {
			@Override
			void openWindow() {
				System.out.println("안방 창문을 엽니다.");
			}
			@Override
			void closeWindow() {
				System.out.println("안방 창문을 닫습니다.");
			}
			
		};
		roomList.add(room);
		roomNo++;
		System.out.println("안방을 만들었습니다. 현재 방의 개수 : " + roomNo);
		
		return room;
		
	}
	
	
	// 작은방 만들고 방번호 return 
	// 방 객체를 만들면서 창문 메서드 재정의
	public int makeSubRoom() {
		room = new Room() {
			@Override
			void openWindow() {
				System.out.println("작은방 창문을 엽니다.");
			}
			@Override
			void closeWindow() {
				System.out.println("작은방 창문을 닫습니다.");
			}
			
		};
		roomList.add(room);
		roomNo++;
		System.out.println("작은방을 만들었습니다. 현재 방의 개수 : " + roomNo);
		
		return roomNo-1;
	}

}
