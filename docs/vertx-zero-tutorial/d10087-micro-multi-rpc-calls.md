# Micro, Multi Rpc Calls

From this chapter the picture will ignore the service details \( Sender/Consumer/EventBus \), show the service
communication only.

## 1. Services

![](/doc/image/d10087-1.png)

Here are four projects in this demo:

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6201 | -- | -- | up-atlas | Common Service |
| 6401 | 6411 | ipc-coeus | up-coeus | Coordinator A |
| 6402 | 6412 | ipc-crius | up-crius | Coordinator B |

## 2. Source Code

### 2.1. MultiApi \( service: up-atlas \)

```java
package up.god.micro.multi;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface MultiApi {
    @Path("ipc/multi/{name}")
    @GET
    @Address("ZERO://RPC/MULTI")
    String sayHello(@PathParam("name") String name);
}
```

### 2.2. MultiWorker \( service: up-atlas \)

```java
package up.god.micro.multi;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class MultiWorker {
    @Address("ZERO://RPC/MULTI")
    @SuppressWarnings("unchecked")
    public Future<JsonObject> sayHello(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        final JsonObject params = new JsonObject().put("name", name);
        return Ux.thenParallelJson(
                Ux.thenRpc("ipc-crius", "RPC://SAY/MULTI", params),
                Ux.thenRpc("ipc-coeus", "RPC://SAY/MULTI", params)
        );
    }
}
```

### 2.3. MultiInsider \( service: up-crius \)

```java
package up.god.ipc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class MultiInsider {
    @Ipc("RPC://SAY/MULTI")
    public Future<JsonObject> sayMulti(final Envelop envelop) {
        final JsonObject data = envelop.data();
        data.put("service", "up-crius");
        return Future.succeededFuture(data);
    }
}
```

### 2.4. MultiInsider \( service: up-coeus \)

```java
package up.god.ipc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class MultiInsider {
    @Ipc("RPC://SAY/MULTI")
    public Future<JsonObject> sayMulti(final Envelop envelop) {
        final JsonObject data = envelop.data();
        data.put("service", "up-coeus");
        return Future.succeededFuture(data);
    }
}
```

## 3. Testing

When you started all above services, you can see more information in console, and then testing with Postman

**URL** : [http://localhost:6100/api/ipc/multi/lang.huan](http://localhost:6100/api/ipc/multi/lang.huan)

**Method** : GET

**Response** :

```java
{
    "data": {
        "0": {
            "name": "lang.huan",
            "service": "up-crius"
        },
        "1": {
            "name": "lang.huan",
            "service": "up-coeus"
        }
    }
}
```

## 4. Summary

This chapter show that one service connect to two rpc services scenarios, you can check more details of this example.
Here you can use `Ux.thenParallelJson` method to merge one or more `Future<JsonObject>` type, also you can modify the
worker as following:

```java
    @Address("ZERO://RPC/MULTI")
    @SuppressWarnings("unchecked")
    public Future<JsonObject> sayHello(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        final JsonObject params = new JsonObject().put("name", name);
        return Ux.thenParallelJson(
                Ux.thenRpc("ipc-crius", "RPC://SAY/MULTI", params),
                Ux.thenRpc("ipc-coeus", "RPC://SAY/MULTI", params)
        ).compose(item -> {
            final JsonObject crius = item.getJsonObject("0");
            final JsonObject coeus = item.getJsonObject("1");
            return Future.succeededFuture(new JsonObject()
                    .put("name", crius.getValue("name"))
                    .put("first", crius.getValue("service"))
                    .put("second", coeus.getValue("service"))
            );
        });
    }
```

Then you should get following response instead of original:

```json
{
    "data": {
        "name": "lang.huan",
        "first": "up-crius",
        "second": "up-coeus"
    }
}
```

> You can do your own calculation based on response from different rpc services, then you can provide correct response
> to client.



