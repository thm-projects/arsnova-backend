# Layers

## Constraints

ARSnova's backend architecture can be separated roughly into three layers which build on one another.
Ordered from highest to lowest these layers are:

1. Controller layer
2. Service layer
3. Persistence layer

When components of another layer are accessed the following constraints have to be satisfied:

* Components of a lower layer **may not** access components of a higher layer.
* Components **may not** directly access components below the next lower layer.

Failure to satisfy these contraints could lead to unintended side-effects because important system aspects, e.g. security and caching, are bypassed.


## Entity system
```plantuml
' You can copy and paste this code section to http://www.plantuml.com/plantuml/ to render the diagram.

package org.springframework.data.repository {
  interface Repository<T, ID>
  interface CrudRepository<T, ID> {
    findById(id: ID): Optional<T>
    findAll(ids: Iterable<ID>): Iterable<T>
    save(entity: T)
    saveAll(entity: Iterable<T>)
    delete(entity: T)
    deleteAll(entities: Iterable<T>)
    ...()
  }
}

package com.fasterxml.jackson.databind {
  class ObjectMapper
}

package model {
  abstract class Entity {
    id: String
    rev: String
    creationTimestamp: Date
    updateTimestamp: Date
    -internal: boolean
  }
  
  Entity <|- ConcreteEntity
}

package controller {
  abstract class AbstractEntityController<E: Entity> {
    get(id: String): E
    getMultiple(ids: Collection<String>): Iterable<E>
    put(entity: E, response: HttpServletResponse): E
    post(entity: E, response: HttpServletResponse): E
    patch(...): E
    delete(id: String)
    find(findQuery: FindQuery<E>): Iterable<E>
    ...()
  }
  AbstractEntityController <|-- ConcreteEntityController : <<bind>>\n<E -> ConcreteEntity>
}

package service {
  interface EntityService<E: Entity> {
    get(id: String): E
    get(ids: Iterable<String>): Iterable<E>
    create(entity: E): E
    update(entity: E): E
    update(oldEntity: E, newEntity: E): E
    patch(entity: E, changes: Map<String, Object>): E
    patch(entities: Iterable<E>, changes: Map<String, Object>): Iterable<E>
    delete(entity: E)
    delete(entities: Iterable<E>
    ...()
  }
  class DefaultEntityServiceImpl<E: Entity> {
    objectMapper: ObjectMapper
  }

  EntityService <|.. DefaultEntityServiceImpl
  DefaultEntityServiceImpl <|-- ConcreteEntityService : <<bind>>\n<E -> ConcreteEntity>
}

package persistence {
  Repository <|-- CrudRepository
  CrudRepository <|.. ConcreteRepository : <<bind>>\n<T -> ConcreteEntity\nID -> String>
}

ConcreteEntityController o- ConcreteEntityService
ConcreteEntityService o- ConcreteRepository

hide circle
'hide empty fields
'hide empty methods
skinparam monochrome true
skinparam shadowing false
@enduml
```
This diagram shows the layers and classes involved in handling entities. Its intention is to give a brief overview, so some elements have been left out for simplicity.
