# Micro, Future with Utility X

This chapter we still stay at the first Rpc example, but we switch our programming style with Utility X to let your rpc
code more simple.

## 1. Services

This demo services should be as following:

![](/doc/image/d10086-1.png)

Here are three projects in current demo:

| Http Port | Ipc Port | Ipc Service Name | Project | Role |
| :--- | :--- | :--- | :--- | :--- |
| 6100 | -- | -- | up-athena | Api Gateway |
| 6201 | -- | -- | up-atlas | Common Service |
| 6401 | 6411 | ipc-coeus | up-coeus | Coordinator A |

## 2. Source Code

### 2.1. FutureApi \( service: up-atlas \)

```java
package up.god.micro.rpc;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface FutureApi {
    @Path("ipc/future/{name}")
    @GET
    @Address("ZERO://RPC/SECOND")
    String sayHello(@PathParam("name") String name);
}
```

### 2.2. FutureWorker \( service: up-atlas \)

```java
package up.god.micro.rpc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class FutureWorker {
    @Address("ZERO://RPC/SECOND")
    public Future<JsonObject> sayHello(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        final JsonObject params = new JsonObject().put("name", name);
        return Ux.thenRpc("ipc-coeus", "RPC://SAY/FUTURE", params);
    }
}
```

### 2.3. HelloInsider \( service: up-coeus \)

Here we list completed rpc services so that developers could compare different styles in rpc service

```java
package up.god.ipc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.typed.Uson;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class HelloInsider {

    @Ipc("RPC://SAY/HELLO")
    public Envelop sayHello(final Envelop envelop) {
        final JsonObject data = envelop.data();
        System.out.println(data);
        return Envelop.success(data);
    }

    // New added method to this service.
    @Ipc("RPC://SAY/FUTURE")
    public Future<JsonObject> sayFuture(final Envelop envelop) {
        final JsonObject data = envelop.data();
        // You can use Jooq here directly 
        return Uson.create(data).toFuture();
    }
}
```

## 3. Testing

Then you can start three services and testing:

**URL** : [http://localhost:6100/api/ipc/future/huan.huan](http://localhost:6100/api/future/huan.huan)

**Method** : GET

**Response** :

```json
{
    "data": {
        "name": "huan.huan"
    }
}
```

## 4. Console

### 4.1. Start Up Console

Here you should focus on some specific logs in console output when services are started up.

**common service: up-atlas**

```shell
    [ Up Micro ] <Application Name> = "zero-istio",
    [ Up Micro ] Configuration Path = /zero/zero-istio/endpoint/routes/up-atlas:10.0.0.6:6201, 
    [ Up Micro ] Service Name = up-atlas,
    [ Up Micro ] EndPoint = http://10.0.0.6:6201
    [ Up Micro ] Route Uris = 
    [ Up Micro ]     /api/ipc/future/:name
    [ Up Micro ]     /api/rpc/:name
    [ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √
```

**coordinator: up-coeus**

```shell
    [ Up Rpc   ] <Application Name> = "zero-istio",
    [ Up Rpc   ] Configuration Rpc Point = /zero/zero-istio/ipc/routes/ipc-coeus:10.0.0.6:6411, 
    [ Up Rpc   ] Service Name = ipc-coeus,
    [ Up Rpc   ] Ipc Channel = grpc://10.0.0.6:6411
    [ Up Rpc   ] Ipc Address = 
    [ Up Rpc √ ]     RPC://SAY/FUTURE
    [ Up Rpc √ ]     RPC://SAY/HELLO
    [ Up Rpc   ] √ Successfully to registered IPCs, wait for community......SUCCESS √
```

### 4.2. Request Flow

**coordinator: up-coeus**

```shell
[ ZERO ] --> ( Terminator ) found, will provide response. ......
......
[ ZERO ] Current flow is Future<T>, return type = class io.vertx.core.impl.SucceededFuture
```

**common service: up-atlas**

```shell
[ ZERO ] ( Rpc Client ) Build channel ( host = 10.0.0.6, port = 6411, hashCode = 461512011 )
[ ZERO ] ( Rpc Client ) Final Traffic Data will be IpcData......
[ ZERO ] ( Rpc Client ) Response Json data is {"name":"huan.huan"}
[ ZERO ] ( Rpc -> thenRpc ) Client = 1864168592, Ipc ( ipc-coeus,RPC://SAY/FUTURE )......
```

## 5. Summary

This chapter programming style is recommend by zero system because all the workflow will be async. Here from the console
output message the developers could know rpc details. Another thing here that developers should know:

* We recommend use `Ux.thenRpc` instead of native `RpcClient` in previous chapter because it's simple and fluent.
* There are node role output in console, but the role here depend on the annotation content, not the role we defined
  here.

In this example we could see our node `up-coeus` is **Coordinator**, but the message output show that:

```shell
[ ZERO ] --> ( Terminator ) found, will provide response. ......
```

Actually here `up-coeus` role is **Terminator** instead of **Coordinator** because this node has not forward service
node.

