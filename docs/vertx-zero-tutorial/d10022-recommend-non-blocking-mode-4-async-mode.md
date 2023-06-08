# Non-Blocking, Mode 4 Async Mode \( Java Style \)

The most useful mode in zero system will be from current chapter, we call it asynchronous mode, it will be used in many
business scenarios. We have defined different programming style for this mode and this tutorial will introduce this mode
in java style.

## 1. Introduction

![](/doc/image/request-mode4.png)There are both sender and consumer in current mode, and the response will reply the
processed result.

```
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker -> Response
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
public class JavaStyleActor {

    @POST
    @Path("request/java")
    @Address("ZERO://ASYNC/JAVA")
    public String sayHell(final JsonObject data) {
        return data.encode();
    }
}
```

### 2.2. Consumer

```java
package up.god.micro.async;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class JavaStyleWorker {

    @Address("ZERO://ASYNC/JAVA")
    public Envelop async(final Envelop input) {
        final String literal = input.data();
        final JsonObject data = new JsonObject()
                .put("result", "SUCCESS")
                .put("input", literal);
        return Envelop.success(data);
    }
}
```

### Programming Style

1. The worker class must be annotated with `io.vertx.up.annotations.Queue`.
2. The worker method signature must be `Envelop xxx(Envelop)`, in this mode this method signature is fixed.
3. You must use `io.vertx.up.annotations.Address` annotation to set address of string type in **Sender/Consumer** both.
4. Address value should be the same shared in **Sender/Consumer**.

_The difference between One Way Mode and Async Mode \( Java Style \) is the method signature of consumer. In One Way
Mode, the client does not care the response data, zero will tell client the response true or false, but in current mode,
zero system will reply the executed returned data._

## 3. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.async.JavaStyleActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] Vert.x zero has found 3 incoming address from the system. Incoming address list as below: 
[ ZERO ]        Addr : ZERO://ASYNC/JAVA
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.async.JavaStyleWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/request/java" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 4. Testing

**URL** : [http://localhost:6083/api/request/java](http://localhost:6083/api/request/java)

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
        "result": "SUCCESS",
        "input": "{\"username\":\"lang.yu\",\"email\":\"lang.yu@hpe.com\"}"
    }
}
```

## 5. Summary

This mode is widely used mode in zero system, except programming style, we recommend to use this mode in your real
environment. The following requirements should be done by worker threads instead of agent only:

* [x] The task that will take a long time to be finished.
* [x] Database accessing.
* [x] Network IO connection/requesting.
* [x] File system accessing.
* [x] Complex business calculating.
* [x] Data size is huge and it may take many resources \( CPU, Memory \).



