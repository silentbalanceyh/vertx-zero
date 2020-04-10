# D10091 - Micro Stream, Consumer Async Mode to Terminator

This chapter we'll introduce consumer of async mode to send request to rpc `Terminator`.

## 1. Service

![](/doc/image/d10091-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

### 2.1. AsyncApi \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface AsyncApi {

    @Path("ipc/stream/async1/{name}")
    @GET
    @Address("ZERO://IPC/NODE/ASYNC1")
    String sayEnvelop(@PathParam("name") String name);
}
```

### 2.2. AsyncWorker \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Queue;

@Queue
public class AsyncWorker {

    @Address("ZERO://IPC/NODE/ASYNC1")
    @Ipc(to = "RPC://IPC/NODE/ASYNC1", name = "ipc-hecate")
    public Future<JsonObject> sayAsync(final JsonObject params) {
        final String name = params.getString("0");
        return Future.succeededFuture(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }
}
```

### 2.3. AsyncInsider \( service: up-hecate \)

```java
package up.god.ipc;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class AsyncInsider {

    @Ipc("RPC://IPC/NODE/ASYNC1")
    public JsonObject sayAsync1(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate")
                .put("type", "async");
    }
}
```

## 3. Testing

Then after you started above three services, you can test this demo:

**URL** : [http://localhost:6100/api/ipc/stream/async1/huan1](http://localhost:6100/api/ipc/stream/async1/huan1)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan1",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "type": "async"
    }
}
```

## 4. Summary

In this demo, the consumer method signature is as following:

```java
public Future<JsonObject> sayAsync(final JsonObject params)
```



