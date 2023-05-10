# Micro Stream, Consumer Sync Mode to Terminator

As we known before, zero system provide different modes for consumer programming style, here we provide Sync mode of
consumer to communicate with Rpc Services.

## 1. Services

![](/doc/image/d10090-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

### 2.1. EnvelopApi \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface EnvelopApi {

    @Path("ipc/stream/envelop1/{name}")
    @GET
    @Address("ZERO://IPC/NODE/ENVELOP1")
    String sayEnvelop(@PathParam("name") String name);
}
```

### 2.2. EnvelopWorker \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class EnvelopWorker {

    @Address("ZERO://IPC/NODE/ENVELOP1")
    @Ipc(to = "RPC://IPC/NODE/ENVELOP1", name = "ipc-hecate")
    public Envelop execute(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return Envelop.success(new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus"));
    }
}
```

### 2.3. EnvelopInsider \( service: up-hecate \)

```java
package up.god.ipc;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class EnvelopInsider {

    @Ipc("RPC://IPC/NODE/ENVELOP1")
    public JsonObject sayEnvelop(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate")
                .put("type", "envelop");
    }
}
```

## 3. Testing

When you started above three services, you can testing this demo

**URL** : [http://localhost:6100/api/ipc/stream/envelop1/huan1](http://localhost:6100/api/ipc/stream/envelop1/huan1)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan1",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "type": "envelop"
    }
}
```

## 4. Summary

This demo data flow is the same as previous, the little difference is the consumer method signature. The consumer in
current tutorial is:

```java
public Envelop execute(final Envelop envelop)
```

As you known it's Sync Mode consumer that zero system defined.

