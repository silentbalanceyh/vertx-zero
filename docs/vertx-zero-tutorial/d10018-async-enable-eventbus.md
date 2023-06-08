# Non-Blocking, Enable EventBus

As we known, all the request operations in vert.x should be async style and the concept of request in vert.x we also
call it "Event", here we'll introduce the core zero **Event Driven Model**. The event-request workflow in zero will be
as following sequence:

1. `@EndPoint` class listened the http port, we often call the role of this class "Agent", it will receive http restful
   request.
2. This class's threads will work in Event Loop of vert.x, then zero system will put the return value of the method
   in `@EndPoint` to wrapper to `Envelop` object \( Uniform Resource Model \), and this operation is invisible for
   developers.
3. Then the `Envelop` object will be sent by `@Address` to event bus.
4. Then the worker threads will consume the `Envelop` message from event bus by the same `@Address` .
5. Finally the worker threads \( That are working in worker pool \) will reply the results to client.

In total the request work flow should be:

```
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker -> Response
```

But for above request workflow, the developers could focus on **Sender** and **Consumer** threads development only, it's
enough to finish business requirement.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

### 1.1. Sender \( In Agent \)

```java
package up.god.micro.request;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api")
@EndPoint
public class AsyncActor {

    @Path("/async/event")
    @POST
    @Address("ZERO://EVENT")    // Event bus address communication
    public JsonObject sendEvent(final JsonObject data) {
        return data;
    }
}
```

### 1.2. Consumer \( In Worker \)

```java
package up.god.micro.request;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class AsyncWorker {

    @Address("ZERO://EVENT")    // Event but address communication
    public Envelop reply(final Envelop envelop) {
        final JsonObject resource = envelop.data();
        System.out.println(resource);
        resource.put("result", "SUCCESS");
        return Envelop.success(resource);
    }
}
```

## 2. Console

There are some differences in async mode in console:

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.request.AsyncActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 1 Queue ) The Zero system has found 1 components of @Queue.
[ ZERO ] Vert.x zero has found 1 incoming address from the system. Incoming address list as below: 
[ ZERO ]        Addr : ZERO://EVENT
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.request.AsyncWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/async/event" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Async Request

**URL** : [http://localhost:6083/api/async/event](http://localhost:6083/api/async/event)

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
        "result": "SUCCESS"
    }
}
```

## 4. Summary

In this tutorial you could know the async request workflow in zero system, it's not created or designed by zero system
but vert.x provided. This mode is high performance mode to process requests, and we recommend to use** Event Bus** to do
some long term works such as database accessing, network processing etc. From this chapter we'll start to move on async
request workflow in zero system.

