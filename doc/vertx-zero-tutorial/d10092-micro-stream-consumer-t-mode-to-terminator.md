## D10092 - Micro Stream, Consumer T Mode to Terminator

This chapter we'll focus on `Dynamic` types with java data.

## 1. Services

![](/doc/image/d10092-1.png)

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6301 | 6311 | ipc-epimetheus | up-epimetheus | Originator |
| 6501 | 6511 | ipc-hecate | up-hecate | Terminator |

## 2. Source Code

### 2.1. DynamicApi \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface DynamicApi {
    @Path("ipc/stream/dynamic1/{name}")
    @GET
    @Address("ZERO://IPC/NODE/DYNAMIC1")
    String sayEnvelop(@PathParam("name") String name);
}
```

> Here for normalizing the parameters we add additional Actor class to this demo, also the reader could go back to check non-interface programming style.

### 2.2. DynamicActor \( service: up-epimetheus \)

```java
package up.god.micro.worker;

public class DynamicActor implements DynamicApi {
    @Override
    public String sayEnvelop(final String name) {
        return name;
    }
}
```

### 2.3. DynamicWorker \( service: up-epimetheus \)

```java
package up.god.micro.worker;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.annotations.Queue;

@Queue
public class DynamicWorker {

    @Address("ZERO://IPC/NODE/DYNAMIC1")
    @Ipc(to = "RPC://IPC/NODE/DYNAMIC1", name = "ipc-hecate")
    public JsonObject sayDynamic(final String name) {
        return new JsonObject()
                .put("name", name)
                .put("originator", "ipc-epimetheus");
    }
}
```

### 2.4. DynamicInsider \( service: up-hecate \)

```java
package up.god.ipc;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class DynamicInsider {

    @Ipc("RPC://IPC/NODE/DYNAMIC1")
    public JsonObject sayDyanmic(final Envelop envelop) {
        final JsonObject data = envelop.data();
        return data.put("terminator", "ipc-hecate")
                .put("type", "dynamic");
    }
}
```

## 3. Testing

After you have started above three services, you can test this demo:

**URL** : [http://localhost:6100/api/ipc/stream/dynamic1/huan1](http://localhost:6100/api/ipc/stream/dynamic1/huan1)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan1",
        "originator": "ipc-epimetheus",
        "terminator": "ipc-hecate",
        "type": "dynamic"
    }
}
```

## 4. Summary

This chapter consumer method signature is more freedom as following:

```java
public JsonObject sayDynamic(final String name)
```

You can use primary type in worker and write any codes here.

