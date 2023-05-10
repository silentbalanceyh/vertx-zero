# Non-Blocking, Mode 3 One Way Mode

From this chapter of the tutorials, we started to introduce `EventBus` enabled mode, the one way mode just like Ping
Mode, but enable the event bus instead of agent only. The data will be send to event bus and it's better to use this
mode to do some async jobs or long time jobs.

## 1. Introduction

![](/doc/image/request-mode3.png)There are both sender and consumer in current mode:

```
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker -> Response ( true / false )
```

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 2. Source Code

### 2.1. Sender

```java
package up.god.micro.request;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class OneWayActor {

    @POST
    @Path("request/one-way")
    @Address("ZERO://ONE-WAY")
    public String process(
            final JsonObject data
    ) {
        return data.encode();
    }
}
```

### 2.2. Consumer

```java
package up.god.micro.request;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class OneWayWorker {

    @Address("ZERO://ONE-WAY")
    public void process(final Envelop envelop) {
        final String item = envelop.data();
        System.out.println(item);
    }
}
```

### Programming Rules

1. The worker class must be annotated with `io.vertx.up.annotations.Queue`.
2. The worker method signature must be `void xxx(Envelop)`, in this mode this method signature is fixed.
3. You must use `io.vertx.up.annotations.Address` annotation to set address of string type in **Sender/Consumer** both.
4. Address value should be the same shared in **Sender/Consumer**.

## 3. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.request.OneWayActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 2 Queue ) The Zero system has found 2 components of @Queue.
[ ZERO ] Vert.x zero has found 2 incoming address from the system. Incoming address list as below: 
......
[ ZERO ]        Addr : ZERO://ONE-WAY
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.request.OneWayWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/request/one-way" has been deployed by ZeroHttpAgent, Options = Route...
```

## 4. Testing

**URL** : [http://localhost:6083/api/request/one-way](http://localhost:6083/api/request/one-way)

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
    "data": true
}
```

You could see output in console as following:

```json
{"username":"lang.yu","email":"lang.yu@hpe.com"}
```

## 5.Summary

This mode just like Ping mode, the data in response is true/false only, the little difference between Ping Mode and
current mode is that current mode enabled event bus and the task has been finished by agent/worker threads both and they
are asynchronous, but Ping mode's works have been finished by single thread of agent.

