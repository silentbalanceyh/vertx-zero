# Non-Blocking, Mode 2 Ping Mode

This mode is simpler than Mode 1 Sync Mode, but we could use this mode to check some remote status such as following
requirement:

* Check whether the record existing or missing.
* Get remote status of some components such as schedulers, timers.

The response of this mode will be true or false only.

## 1. Introduction

This mode is also another non-event bus mode, it could be finished by agent \( `@EndPoint` \) only, the workflow should
be as following:

![](/doc/image/request-mode2.png)

This mode workflow is the same as Sync Mode except the response data, It could be used in some special requirement only.
In this mode zero system tell the client whether the working is correct or wrong and won't provide any data in web
response.

There is no consumer role in this workflow, but sender instead:

```
Request -> Agent -> @EndPoint ( Sender ) -> Response ( true/false )
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
public class PingModeActor {

    @Path("request/ping")
    @GET
    public void check() {
        System.out.println("Hello, I'm working well.");
    }

    @Path("request/ping-false")
    @GET
    public boolean checkStatus() {
        System.out.println("Hello, I'm working well and return false.");
        return false;
    }
}
```

### Programming Rules

1. The sender class should be annotated with `io.vertx.up.annotations.EndPoint`.
2. Other annotations all belong to JSR311, you should set one http method annotation to java method.
3. The java method return type must be `void`or `boolean` , it's the only difference from Sync Mode.
4. Do not use `@Address` annotation, this mode should disable event bus.

## 3. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.request.PingModeActor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/request/ping" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/request/ping-false" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 4. Testing

### 4.1. Ping Request

**URL** : [http://localhost:6083/api/request/ping](http://localhost:6083/api/request/ping)

**Method** : GET

**Response** :

```json
{
    "data": true
}
```

### 4.2. Ping false Request

**URL** : [http://localhost:6083/api/request/ping-false](http://localhost:6083/api/request/ping-false)

**Method** : GET

**Response** :

```json
{
    "data": false
}
```

## 5. Summary

In current version of zero, this mode only support two response value: true/false, if you want to get more information
from remote zero system, you should use Sync Mode instead.

