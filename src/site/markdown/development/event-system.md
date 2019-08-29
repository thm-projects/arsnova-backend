# Event System

ARSnova's event system allows components to act upon state changes without requiring complex bean dependencies.
Events offer a way to dynamically update data without the need to poll the database for changes.
For instance, if clients are interested in the number of currently available contents,
the respective component could initialize the number using the database, while keeping it up to date using events.

Events are published before and after all CRUD operations of entities which are handled by `DefaultEntityServiceImpl`.
Those `CrudEvent`s contain the entity and, based on the operation specific implementation, additional details like the changes applied to an entity.

Before you start working with events you should first read
[Spring's documentation](https://docs.spring.io/spring-framework/docs/5.1.x/spring-framework-reference/core.html#context-functionality-events)
about them.


## Receiving events

Events can be received by any component by adding a listener.
A listener is a simple method which is annotated with `@EventListener` and has a single parameter, the event.
The following code snipped shows how an EventListener on the `BeforeDeletionEvent` of `UserProfile`s is registered:

```java
@EventListener
public void handleUserDeletion(final BeforeDeletionEvent<UserProfile> event) {
	final Iterable<Room> rooms = getRoomsForUserId(event.getEntity().getId());
	delete(rooms);
}
```


## Sending events

Events are published through the `ApplicationEventPublisher` bean.
This bean can be injected to any component by either implementing Spring's `ApplicationEventPublisherAware` interface or using constructor injection.
Once the component is aware of the publisher, events be sent via its `publishEvent` method.
To be eligible as a event type, a class has to extend Spring's `ApplicationEvent`.

_Note_: Events are sent and received on the same thread, i.e., it is a synchronous operation.
