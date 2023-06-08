# Non-Blocking, Mode 1 Sync Mode

Zero system support many request modes as smart flow, this tutorial describe the first mode that often appeared in
previous tutorials.

## 1. Introduction

This mode is standard non-event bus mode, it won't enable event bus, but agent \( `@EndPoint` \) only. The workflow
should be as following:

![](/doc/image/request-mode1.png)

Agent threads are running in event loop thread pool of vert.x, in this mode the workflow should be:

1. The client send web request to zero system.
2. The routing sub-system of zero will match the web request uri to look up correct routing handler.
3. Routing handler will execute the mounted java function \( Defined by developer \).
4. After java function executed, zero system will convert return value of the function to web response and send to
   client.

There is no consumer role in this workflow, but sender instead:

```
Request -> Agent -> @EndPoint ( Sender ) -> Response
```

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 2. Source Code

```java
package up.god.micro.request;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class SyncModeActor {

    @Path("request/sync")
    @GET
    public String sayHello() {
        return "Hello Sync Mode.";
    }
}
```

### Programming Rules

1. The sender class should be annotated with `io.vertx.up.annotations.EndPoint`.
2. Other annotations all belong to JSR311, you should set one http method annotation to java method.
3. The java method return type must not be `void`.
4. Do not use `@Address` annotation, this mode should disable event bus.

## 3. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.request.SyncModeActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/request/sync" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 4. Testing

**URL** : [http://localhost:6083/api/request/sync](http://localhost:6083/api/request/sync)

**Method** : GET

**Response**:

```json
{
    "data": "Hello Sync Mode."
}
```

## 5. Summary

In zero programming specification, we often named this class suffix with `Actor`, it means that this actor is working in
agent thread, as an actor it will dispatch the request or process request. In the mode of current tutorial, this actor
process the web request directly.

