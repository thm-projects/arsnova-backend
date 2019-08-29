# Caching

Please read about Spring Framework's [Cache Abstraction](https://docs.spring.io/spring/docs/5.1.x/spring-framework-reference/integration.html#cache) first.


## What should get cached?

The short answer: All data that is rarely written but frequently read.
Caching is already handled for all entities as long as they are accessed through the `DefaultEntityServiceImpl`.
Additional, specialized caching can be helpful for aliases (e.g. mapping a room's `shortId` the `id`) or find query results.

While caching provides an opportunity to greatly speed up the execution of various requests, it does come with a price:
You have to think of all cases were the cached data might become stale.


## Generating cache keys

By default, the cache key is generated from all method parameters.
If some of the parameters should not affect cache handling, the key parameter of the caching annotation has to be set.
For reference types which are used as part of the key you have to make sure that `hashCode` and `equals` methods are overriden.
Be aware though that you need to carefully choose the fields which should be part of the `equals`/`hashCode`:
In case of CouchDB, for example, it is not a good idea to use a document's `rev` field.
Every time a document is updated, it gets a new `rev` which will make it _unequal_ to all its previous versions,
making cache updates using `@CachePut` impossible.


## Event-based cache updates

[ARSnova's event system](event-system.md) provides a useful way for fine-grained cache updates because the events contain all relevant domain objects.
If you need to update a cache based on one of ARSnova's events, you can combine `@EventListener` with one of the caching annotations.


## List of caches

Here is a list of all caches, their keys, and a short description.

_Note_: With the introduction of the generic `entity` cache many of the other entity related caches became obsolete and will be removed in the future.

Cache name | Key | Description
-----------|-----|------------
`entity` | entity type + `-` + id | Contains all entity objects handled by `DefaultEntityServiceImpl`.
`contentlists`| database id of room | Contains all contents for the specified room irrespective of their variant.
`lecturecontentlists` | database id of room | Contains all "lecture" variant contents for the specified room.
`preparationcontentlists` | database id of room | Contains all "preparation" variant contents for the specified room.
`flashcardcontentlists` | database id of room | Contains all "flashcard" variant contents for the specified room.
`contents` | `Content` entity | Contains single content objects.
`contents` | database id of content | Although it shares the name of the previously mentioned cache, it is in essence a different cache because the keys are different. This means that the same `Content` object might be associated with two different keys.
`answerlists`| database id of content | Contains single answer objects.
`score` | `Room` entity | Contains `CourseScore` objects to calculate the score values for the specified room.
`rooms` | keyword of room | Contains rooms identified by their keywords.
`rooms` | database id of room | Although it shares the name of the previously mentioned cache, it is in essence a different cache because the keys are different. This means that the same `Room` object might be associated with two different keys.
`statistics` | -- | Contains a single, global statistics object.
