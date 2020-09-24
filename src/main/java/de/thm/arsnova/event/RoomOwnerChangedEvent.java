package de.thm.arsnova.event;

public class RoomOwnerChangedEvent {
	public enum Operation {
		CREATED,
		UPDATED,
		DELETED
	}

	private String roomId;
	private String userId;
	private String operation;

	public RoomOwnerChangedEvent() {
	}

	public RoomOwnerChangedEvent(final String roomId, final String userId, final Operation operation) {
		this.roomId = roomId;
		this.userId = userId;
		this.operation = operation.name();
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(final String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(final String userId) {
		this.userId = userId;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(final Operation operation) {
		this.operation = operation.name();
	}
}
