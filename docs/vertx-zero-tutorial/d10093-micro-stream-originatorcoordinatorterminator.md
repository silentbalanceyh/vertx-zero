# Micro Stream, Originator/Coordinator/Terminator

This chapter we'll introduce a complex example of stream mode, based on this example you can build basic mesh service
data flow in zero system.

## 1. Services

![](/doc/image/d10093-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6401 | 6411 | ipc-coeus | up-coeus | Coordinator A |
| 6402 | 6412 | ipc-crius | up-crius | Coordinator B |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

### 2.1. HuttApi \( service: up-epimetheus \)

```java
package up.god.micro.mesh;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface HuttApi {

    @Path("ipc/mesh/hutt/{name}")
    @GET
    @Address("ZERO://IPC/NODE/HUTT")
    String sayHutt(@PathParam("name") String name);
}
```

### 2.2. HuttWorker \( service: up-epimetheus \)

```java
package up.god.micro.mesh;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class HuttWorker {

    @Address("ZERO://IPC/NODE/HUTT")
    @Ipc(to = "RPC://IPC/NODE/HUTT", name = "ipc-coeus")
    public Future<JsonObject> sayHutt(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return Future.succeededFuture(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }
}
```

### 2.3. HuttInsider \( service: up-coeus \)

```java
package up.god.ipc.mesh;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class HuttInsider {
    @Ipc(value = "RPC://IPC/NODE/HUTT",
            to = "RPC://IPC/NODE/HUTT1", name = "ipc-crius")
    public String next(final Envelop envelop) {
        final JsonObject data = envelop.data();
        data.put("coordinator1", "ipc-coeus");
        return data.encode();
    }
}
```

### 2.4. HuttInsider \( service: up-crius \)

```java
package up.god.ipc.mesh;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class HuttInsider {
    @Ipc(value = "RPC://IPC/NODE/HUTT1",
            to = "RPC://IPC/NODE/HUTTS", name = "ipc-hecate")
    public JsonObject next(final Envelop envelop) {
        final String content = envelop.data();
        return new JsonObject(content)
                .put("coordinator2", "ipc-cronus");
    }

}
```

### 2.5. HuttInsider \( service: up-hecate \)

```java
package up.god.ipc.mesh;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class HuttInsider {

    @Ipc(value = "RPC://IPC/NODE/HUTTS")
    public JsonObject sayHutt(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate")
                .put("type", "async");
    }
}
```

> Here please be careful about different role configuration for stream, you can
> refer [D10083 - Micro, Rpc Mode](d10083-micro-rpc-mode.md) to check different role configuration tutorials.

## 3. Testing

Here you must start five services and mount to Api Gateway and then you can test current demo as following:

**URL** : http://localhost:6100/api/ipc/mesh/hutt/huan1

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan1",
        "originator": "ipc-epimetheus",
        "coordinator1": "ipc-coeus",
        "coordinator2": "ipc-cronus",
        "terminator": "ipc-hecate",
        "type": "async"
    }
}
```

## 4. Summary

Based on above response data you can see the data flow went through four nodes, the point that you should know is that
in this mode the data flow could not be revert, it means that the direction is `Single`, you can pass the data to next
service node only.



