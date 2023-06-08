# Non-Blocking, Mode 5 Async Mode \( vert.x style \)

This mode is very important in zero system, because we prefer to recommend use this mode in your development. Because
vert.x is non-blocking and async, this mode is based on vert.x async multi threads, if you use this mode to do
development works, you can be very smart to finish all the business requirements.

From architecture of zero system, this mode contains following advantages:

* [x] You can use all the native async clients that vert.x provided directly such
  as `MongoClient, MySqlClient, RedisClient`etc.
* [x] All the request works should be async mode and the performance is better.
* [x] You can do some reactive programming with `Rxjava2` instead of others.
* [x] You can use `UtilityX` package that zero system provided to do complex business requirements or frequently
  requirements.

# 1. Introduction

![](/doc/image/request-mode5.png)The workflow of this mode is the same as Mode 4, but there are some difference in
programming. There are both sender and consumer in current mode, and the response will reply the processed result.

```
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer with MessageHandler ) -> Worker -> Response
```

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 2. Source Code

### 2.1. Sender

```java
package up.god.micro.async;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class VertxStyleActor {

    @POST
    @Path("request/vertx/handler")
    @Address("ZERO://ASYNC/VERTX/HANDLER")
    public JsonObject sayHandler(final JsonObject data) {
        data.put("agent", "HANDLER");
        return data;
    }

    @POST
    @Path("request/vertx/future")
    @Address("ZERO://ASYNC/VERTX/FUTURE")
    public JsonObject sayFuture(final JsonObject data) {
        data.put("agent", "FUTURE");
        return data;
    }
}
```

### 2.2. Consumer

```java
package up.god.micro.async;

import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class VertxStyleWorker {


    @Address("ZERO://ASYNC/VERTX/HANDLER")
    public void sayMessage(final Message<Envelop> message) {
        final JsonObject data = Ux.getBody(message);
        message.reply(Envelop.success(data));
    }

    @Address("ZERO://ASYNC/VERTX/FUTURE")
    public Future<JsonObject> sayFuture(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return Future.succeededFuture(data);
    }
}
```

### Programming Rules

1. The worker class must be annotated with `io.vertx.up.annotations.Queue`.
2. You must use `io.vertx.up.annotations.Address` annotation to set address of string type in **Sender/Consumer** both.
3. Address value should be the same shared in **Sender/Consumer**.
4. The worker method signature should be as following:
    1. `void method(Message<Envelop>)`
    2. `Future<T> method(Envelop)`
    3. `Future<Envelop> method(Envelop)`

## 3. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.async.VertxStyleActor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 6 Queue ) The Zero system has found 6 components of @Queue.
......
[ ZERO ]        Addr : ZERO://ASYNC/VERTX/FUTURE
[ ZERO ]        Addr : ZERO://ASYNC/VERTX/HANDLER
......
[ ZERO ] ( 2 Receipt ) The queue up.god.micro.async.VertxStyleWorker scanned 2 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/request/vertx/future" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/request/vertx/handler" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 4. Testing

### 4.1. Message Request

**URL** : [http://localhost:6083/api/request/vertx/handler](http://localhost:6083/api/request/vertx/handler)

**Method** : POST

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
        "username": "lang.yu",
        "email": "lang.yu@hpe.com",
        "agent": "HANDLER"
    }
}
```

### 4.2. Future Request

**URL** : [http://localhost:6083/api/request/vertx/future](http://localhost:6083/api/request/vertx/future)

**Method** : POST

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
        "username": "lang.yu",
        "email": "lang.yu@hpe.com",
        "agent": "FUTURE"
    }
}
```

## 5. Summary

Current mode is standard vert.x mode and we recommend to use this mode in your project. Here are a java class
named `Envelop` and it's Uniform Resource Model that defined by zero system, it could take many information to go
through from agent to worker threads on event bus. The Uniform Resource Model will be introduced in future tutorials, it
could help developers to simply the programming in web container, service specification and restful web request, then
the developers could focus on business requirements only.

