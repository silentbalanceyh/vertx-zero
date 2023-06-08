# JSR330, @Inject One Implementation

From previous tutorials, zero system support following three injection codes.

* [x] Simple Java Object Injection
* [x] One interface and one java implementation object Injection
* [x] One interface and multi java implementation objects Injection

This chapter will describe the second mode that the structure should be one interface and one implementation object. The
workflow of this example should be:

```shell
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker 
                                    -> Stub -> Service -> Response
```

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

### 1.1. Sender

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class OneActor {

    @Path("inject/one")
    @PUT
    @Address("ZERO://INJECT/ONE")
    public JsonObject sayInject(final JsonObject data
    ) {
        return new JsonObject()
                .put("age", 33)
                .mergeIn(data);
    }
}
```

### 1.2. Consumer

```java
package up.god.micro.inject;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import jakarta.inject.Inject;

@Queue
public class OneWorker {

    @Inject
    private transient OneStub stub;

    @Address("ZERO://INJECT/ONE")
    public Future<JsonObject> process(final JsonObject user) {
        final JsonObject processed = this.stub.getData(user);
        return Future.succeededFuture(processed);
    }
}
```

### 1.3. Stub \( Service Layer Interface \)

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

public interface OneStub {

    JsonObject getData(JsonObject input);
}
```

### 1.4. Stub \( Service Implementation \)

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

public class OneService implements OneStub {
    @Override
    public JsonObject getData(final JsonObject input) {
        input.put("className", getClass().getName());
        return input;
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.inject.OneActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 8 Queue ) The Zero system has found 8 components of @Queue.
[ ZERO ] Vert.x zero has found 10 incoming address from the system. Incoming address list as below: 
......
[ ZERO ]        Addr : ZERO://INJECT/ONE
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.request.OneWayWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
[ ZERO ] ( Field ) Class "class up.god.micro.inject.OneWorker" scanned field = "stub" of \
    up.god.micro.inject.OneService annotated with interface jakarta.inject.Inject. \
        will be initialized with DI container.
......
[ ZERO ] ( Uri Register ) "/api/inject/one" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

**URL** : [http://localhost:6083/api/inject/one](http://localhost:6083/api/inject/one)

**Method** : PUT

**Request** :

```json
{
    "username":"lang.yu",
    "email":"lang.yu@hpe.com"
}
```

**Response** :

```json
{
    "data": {
        "age": 33,
        "username": "lang.yu",
        "email": "lang.yu@hpe.com",
        "className": "up.god.micro.inject.OneService"
    }
}
```

## 4. Summary

This feature is only used when the implementation class is unique, it means that there is only one implementation class
reflect to the interface. If you have multi implementation classes in zero system, how to do it and how to resolve this
kind of situation ? We'll move to the next chapter to see another situations.



