# Non-Blocking, Mode 5 Experimental extension

In zero system programming, the mode 5 is recommend because it could release the power of vert.x. For some special
business requirements the developers want to return to Java style to do programming, based on this situation we also
developed the advanced mode 5, call it experimental version. This version could let the developer more freedom to do
programming in zero system, although we provide this mode, zero system still recommend the developers to use Uniform
Resource Model that defined by zero system because it contains more standard data here.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

### 1.1. Sender

```java
package up.god.micro.async;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class VertxAsyncActor {
    @POST
    @Path("request/vertx/futureT")
    @Address("ZERO://ASYNC/VERTX/FUTURE_T")
    public JsonObject sayFutureT(final JsonObject data) {
        data.put("agent", "T");
        return data;
    }
}
```

### 1.2. Consumer

```java
package up.god.micro.async;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

@Queue
public class VertxAsyncWorker {

    @Address("ZERO://ASYNC/VERTX/FUTURE_T")
    public Future<JsonObject> sayFutureT(final JsonObject data) {
        final String string = data.encode();
        System.out.println(string);
        data.put("result", "Perfect");
        return Future.succeededFuture(data);
    }
}
```

### Programming Rules

1. The worker class must be annotated with `io.vertx.up.annotations.Queue`.
2. You must use `io.vertx.up.annotations.Address` annotation to set address of string type in **Sender/Consumer** both.
3. Address value should be the same shared in **Sender/Consumer.**
4. The biggest difference between standard mode 5 and current mode is that there is no limitation in current mode, we
   could be sure the method signature should be: `Future<T> method( I )`, except the return type should
   be: `io.vertx.core.Future`, other programming parts are almost the same as java programming.

## 2. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.async.VertxAsyncActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 6 Queue ) The Zero system has found 6 components of @Queue.
[ ZERO ] Vert.x zero has found 8 incoming address from the system. Incoming address list as below: 
......
[ ZERO ]        Addr : ZERO://ASYNC/VERTX/FUTURE_T
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.async.VertxAsyncWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/request/vertx/futureT" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

**URL** : http://localhost:6083/api/request/vertx/futureT

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
        "agent": "T",
        "result": "Perfect"
    }
}
```

## 4. Summary

This mode is in experimental phase because it may contain some bugs here, but in our projects we have put it into
production environment to verify this features. Also this mode is recommend by our team because it's more the same as
java programming.



