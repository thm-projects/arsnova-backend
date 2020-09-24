package de.thm.arsnova.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.thm.arsnova.config.RabbitConfig;
import de.thm.arsnova.config.properties.MessageBrokerProperties;
import de.thm.arsnova.model.Room;

@Component
@ConditionalOnProperty(
		name = RabbitConfig.RabbitConfigProperties.RABBIT_ENABLED,
		prefix = MessageBrokerProperties.PREFIX,
		havingValue = "true")
public class RoomOwnerEventDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(RoomOwnerEventDispatcher.class);

	public static final String ROOM_OWNER_CHANGED_QUEUE = "backend.event.room.owner.changed";

	private final RabbitTemplate messagingTemplate;

	@Autowired
	public RoomOwnerEventDispatcher(
			final RabbitTemplate rabbitTemplate
	) {
		messagingTemplate = rabbitTemplate;
	}

	@EventListener
	public void handleAfterCreationEventForRoom(final AfterCreationEvent<Room> event) {
		logger.debug("Handling event: {}", event);

		final Room room = event.getEntity();

		final RoomOwnerChangedEvent mqEvent = new RoomOwnerChangedEvent(
				room.getId(),
				room.getOwnerId(),
				RoomOwnerChangedEvent.Operation.CREATED
		);

		sendEventToQueue(mqEvent);
	}

	@EventListener
	public void handleAfterUpdateEventForRoom(final AfterUpdateEvent<Room> event) {
		logger.debug("Handling event: {}", event);

		final Room oldRoom = event.getOldEntity();
		final Room newRoom = event.getEntity();

		if (!oldRoom.getOwnerId().equals(newRoom.getOwnerId())) {
			final RoomOwnerChangedEvent mqEvent = new RoomOwnerChangedEvent(
					oldRoom.getId(),
					newRoom.getOwnerId(),
					RoomOwnerChangedEvent.Operation.UPDATED
			);

			sendEventToQueue(mqEvent);
		}
	}

	@EventListener
	public void handleAfterDeletionEventForRoom(final AfterDeletionEvent<Room> event) {
		logger.debug("Handling event: {}", event);

		final Room room = event.getEntity();

		final RoomOwnerChangedEvent mqEvent = new RoomOwnerChangedEvent(
				room.getId(),
				room.getOwnerId(),
				RoomOwnerChangedEvent.Operation.DELETED
		);

		sendEventToQueue(mqEvent);
	}

	private void sendEventToQueue(final RoomOwnerChangedEvent event) {
		logger.debug("Sending event: {}, queue: {}", event, ROOM_OWNER_CHANGED_QUEUE);

		messagingTemplate.convertAndSend(
				ROOM_OWNER_CHANGED_QUEUE,
				event
		);
	}
}
