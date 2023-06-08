# Zero JSR330, @Inject Multi Implementation

From previous tutorials, zero system support following three injection codes.

* [x] Simple Java Object Injection
* [x] One interface and one java implementation object Injection
* [x] One interface and multi java implementation objects Injection

This chapter will describe the last mode that the structure should be one interface with multi implementation objects.
The workflow of this example should be:

```shell
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker 
                                    -> Stub -> ServiceA ( Service B ) -> Response
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
public class MultiActor {

    @Path("inject/multi")
    @PUT
    @Address("ZERO://INJECT/MULTI")
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
import io.vertx.up.annotations.Qualifier;
import io.vertx.up.annotations.Queue;

import jakarta.inject.Inject;

@Queue
public class MultiWorker {

    @Inject
    @Qualifier("ServiceB")
    private transient MultiStub stub;

    @Address("ZERO://INJECT/MULTI")
    public Future<JsonObject> process(final JsonObject user) {
        final JsonObject processed = this.stub.getData(user);
        return Future.succeededFuture(processed);
    }
}
```

### 1.3. Stub \( Service Interface \)

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

public interface MultiStub {

    JsonObject getData(JsonObject input);
}
```

### 1.4. Service \( Service A Implementation \)

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

import import jakarta.inject.Named;

@Named("ServiceA")
public class MultiServiceA implements MultiStub {
    @Override
    public JsonObject getData(final JsonObject input) {
        input.put("className", getClass().getName());
        return input;
    }
}
```

### 1.5. Service \( Service B Implementation \)

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

import import jakarta.inject.Named;

@Named("ServiceB")
public class MultiServiceB implements MultiStub {
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
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.inject.MultiActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 9 Queue ) The Zero system has found 9 components of @Queue.
[ ZERO ] Vert.x zero has found 11 incoming address from the system. Incoming address list as below: 
......
[ ZERO ]        Addr : ZERO://INJECT/MULTI
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.inject.MultiWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( 1 Inject ) The Zero system has found "up.god.micro.inject.MultiWorker" object \
    contains 1 components of @Inject or ( javax.inject.infix.* ).
......
[ ZERO ] ( Uri Register ) "/api/inject/multi" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

**URL**: http://localhost:6083/api/inject/multi

**Method**: PUT

**Request**:

```json
{
	"username":"lang.yu",
	"email":"lang.yu@hpe.com"
}
```

**Response**:

```json
{
    "data": {
        "age": 33,
        "username": "lang.yu",
        "email": "lang.yu@hpe.com",
        "className": "up.god.micro.inject.MultiServiceB"
    }
}
```

From the testing result we could see the inject object is "ServiceB".

## 4. Summary

This chapter described the usage of another two JSR330 \( Include extended \) annotations:

* `import jakarta.inject.Named`
* `io.vertx.up.annotation.Qualifier`

Above two annotations could resolve the situation that one interface contains more than one implementation class
injection based on JSR330. But one thing you should know that it's not for switching between different implementations
but for some special design in the system.



