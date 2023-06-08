# Micro Stream, Sender \( Agent \) to Terminator

From this chapter we'll focus on rpc stream mode, this mode we'll meet different role \( method \) such
as `Originator, Coordinator, Terminator` etc.

## 1. Services

![](/doc/image/d10088-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

> We have introduced interface style to ignore Actor, it's disabled in rpc communication, it means that if you send rpc
> request from Sender \( Agent \), you could not send the message to internal Consumer \( Worker \), the interface style
> is used in communication between Sender and Consumer.

### 2.1. SimpleApi \( service: up-epimetheus \)

```java
package up.god.micro.agent;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.Ipc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface SimpleApi {

    @Path("ipc/stream/agent1/{name}")
    @GET
    @Ipc(to = "RPC://IPC/NODE/AGENT1", name = "ipc-hecate")
    JsonObject saySimple(@PathParam("name") String name);
}
```

### 2.2. SimpleActor \( service: up-epimetheus \)

```java
package up.god.micro.agent;

import io.vertx.core.json.JsonObject;

public class SimpleActor implements SimpleApi {
    @Override
    public JsonObject saySimple(final String name) {
        return new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus");
    }
}
```

### 2.3. SimpleInsider \( service: up-hecate \)

```java
package up.god.ipc;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class SimpleInsider {

    @Ipc("RPC://IPC/NODE/AGENT1")
    public JsonObject saySimple(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate");
    }
}
```

## 3. Testing

Once you started above three services, you can test this service as following:

**URL** : [http://localhost:6100/api/ipc/stream/agent1/huan](http://localhost:6100/api/ipc/stream/agent1/huan)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate"
    }
}
```

## 4. Summary

From above testing you could see that rpc between `Originator` and `Terminator` has been communicated successfully, it's
very simple demo to show two roles communication between two roles.

