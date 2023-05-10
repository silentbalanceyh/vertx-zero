# Micro Stream, Consumer \( Worker \) to Terminator

From this chapter we'll focus on rpc stream mode, this mode we'll meet different role \( method \) such
as `Originator, Coordinator, Terminator` etc. This chapter tutorial is different
from [D10088 - Micro Stream, Sender \( Agent \) to Terminator](d10088-micro-stream-sender-agent-to-terminator.md),

* In D10088, The `Originator` is Sender \( Agent \), it means that the request came from `eventloop` of vert.x;
* In current tutorial, The `Originator` is Consumer \( Worker \), it means that the request came from `workerpool` of
  vert.x;

## 1. Services

![](/doc/image/d10089-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

### 2.1. ConsumeApi \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface ConsumeApi {

    @Path("ipc/stream/consumer1/{name}")
    @GET
    @Address("ZERO://IPC/NODE/WORKER1")
    String sayWorker1(@PathParam("name") String name);

    @Path("ipc/stream/consumer2/{name}")
    @GET
    @Address("ZERO://IPC/NODE/WORKER2")
    String sayWorker2(@PathParam("name") String name);

    @Path("ipc/stream/consumer3/{name}")
    @GET
    @Address("ZERO://IPC/NODE/WORKER3")
    String sayWorker3(@PathParam("name") String name);
}
```

### 2.2. ConsumeWorker \( service: up-epimetheus \)

> Future mode used in current tutorials

```java
package up.god.micro.worker;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class ConsumeWorker {

    @Address("ZERO://IPC/NODE/WORKER1")
    @Ipc(to = "RPC://IPC/NODE/WORKER1", name = "ipc-hecate")
    public Future<JsonObject> worker1(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return Future.succeededFuture(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }

    @Address("ZERO://IPC/NODE/WORKER2")
    @Ipc(to = "RPC://IPC/NODE/WORKER2", name = "ipc-hecate")
    public Future<JsonObject> worker2(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return Future.succeededFuture(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }

    @Address("ZERO://IPC/NODE/WORKER3")
    @Ipc(to = "RPC://IPC/NODE/WORKER3", name = "ipc-hecate")
    public Future<JsonObject> worker3(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return Future.succeededFuture(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }
}
```

Here are some rules when you use current tutorial knowledge to write worker:

1. In Sender \( Agent \), the annotations `@Ipc` and `@Address` could not be used at the same time, but here it's
   allowed.
2. The method must contains return value, you should not use `void` for the consumer method.
3. In current tutorials, the return type of `Consumer` methods are all `Future<T>` instead of others.

### 2.3. WorkerInsider \( service: up-hecate \)

```java
package up.god.ipc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class WorkerInsider {
    @Ipc("RPC://IPC/NODE/WORKER1")
    public JsonObject sayWorker1(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate")
                .put("worker", "worker1");
    }

    @Ipc("RPC://IPC/NODE/WORKER2")
    public Envelop sayWorker2(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return Envelop.success(data
                .put("terminator", "ipc-hecate")
                .put("worker", "worker2"));
    }

    @Ipc("RPC://IPC/NODE/WORKER3")
    public Future<JsonObject> sayWorker3(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return Future.succeededFuture(data
                .put("terminator", "ipc-hecate")
                .put("worker", "worker3"));
    }
}
```

## 3. Testing

Then when you start three services, you can do the testing:

### 3.1. Case 1

**URL** : [http://localhost:6100/api/ipc/stream/consumer1/huan1](http://localhost:6100/api/ipc/stream/consumer1/huan1)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan1",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "worker": "worker1"
    }
}
```

### 3.2. Case 2

**URL** : [http://localhost:6100/api/ipc/stream/consumer2/huan2](http://localhost:6100/api/ipc/stream/consumer2/huan2)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan2",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "worker": "worker2"
    }
}
```

### 3.3. Case 3

**URL** : [http://localhost:6100/api/ipc/stream/consumer3/huan3](http://localhost:6100/api/ipc/stream/consumer3/huan3)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan3",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "worker": "worker3"
    }
}
```

## 4. Summary

Here we provide three demos to show how to write the method in `Rpc Server`, except the input data type `Envelop`, you
can choose different return type of the methods as you want. The method signature is as following:

```java
public Future<JsonObject> worker1(final Envelop envelop)
```



