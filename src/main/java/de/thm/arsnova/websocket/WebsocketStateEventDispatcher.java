package de.thm.arsnova.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import de.thm.arsnova.event.StateChangeEvent;
import de.thm.arsnova.model.Content;
import de.thm.arsnova.model.ContentGroup;

@Component
@Profile("!test")
public class WebsocketStateEventDispatcher {
	private static final Logger logger = LoggerFactory.getLogger(WebsocketStateEventDispatcher.class);
	private final RabbitTemplate messagingTemplate;

	public WebsocketStateEventDispatcher(final RabbitTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@EventListener(condition = "#event.stateName == 'state'")
	public <T> void dispatchContentStateEvent(final StateChangeEvent<? extends Content, T> event) {
		logger.debug("Dispatching state event for content: {}", event);
		final String roomId = event.getEntity().getRoomId();
		final WebsocketStateChangeEvent<T> stateEvent = new WebsocketStateChangeEvent<>(
				event.getEntity().getId(),
				event.getStateName(),
				event.getNewValue());
		messagingTemplate.convertAndSend(
				"amq.topic",
				roomId + ".content.state.stream",
				stateEvent
		);
	}

	@EventListener
	public <T> void dispatchContentGroupUpdateEvent(final StateChangeEvent<ContentGroup, T> event) {
		logger.debug("Dispatching state event for content group: {}", event);
		final String roomId = event.getEntity().getRoomId();
		final WebsocketStateChangeEvent<T> stateEvent = new WebsocketStateChangeEvent<>(
				event.getEntity().getId(),
				event.getStateName(),
				event.getNewValue());
		messagingTemplate.convertAndSend(
				"amq.topic",
				roomId + ".contentgroup.state.stream",
				stateEvent
		);
	}

	private static class WebsocketStateChangeEvent<V> {
		private String id;
		private String key;
		private V value;

		private WebsocketStateChangeEvent(final String id, final String key, final V value) {
			this.id = id;
			this.key = key;
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public String getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}
	}
}
