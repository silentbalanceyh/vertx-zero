# JSR330, @Inject Simple Java Object

Until now, the tutorials have introduced the usage of JSR311, Non-Blocking parts, then the tutorials will describe
another Java Specification [JSR330 - Dependency Injection](https://jcp.org/en/jsr/detail?id=330). In current version of
zero system, it support some parts of JSR330, here are the features that zero system supported:

* [x] `jakarta.inject.Inject` annotation
* [x] `import jakarta.inject.Named` annotation
* [x] `io.vertx.up.annotations.Qualifier` extend annotation

For above three annotations, zero system support following features

* [x] Simple Java Object Injection
* [x] One interface and one java implementation object Injection
* [x] One interface and multi java implementation objects Injection

Based on above three points, zero system contains some limitation to implement this JSR.

1. The data object such as POJO could not be used with injection in zero system. **Do not use!**
2. All the injected java object in zero system is singleton, it's not needed to use `javax.inject.Singleton` annotation
   to mark.

Current chapter will introduce simple java object injection firstly.

```
Request -> Agent -> @Address ( Sender ) -> 
    EventBus -> 
        @Address ( Consumer ) -> Worker 
                                    -> Simple Object -> Response
```

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

### 1.1. Sender

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public class SimpleActor {

    @Path("inject/simple")
    @GET
    @Address("ZERO://INJECT/SIMPLE")
    public JsonObject sayInject(
            @QueryParam("username") final String username
    ) {
        return new JsonObject()
                .put("age", 33)
                .put("username", username);
    }
}
```

### 1.2. Consumer

```java
package up.god.micro.inject;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import jakarta.inject.Inject;

@Queue
public class SimpleWorker {

    @Inject
    private transient SimpleObject simple;

    @Address("ZERO://INJECT/SIMPLE")
    public Future<String> process(final JsonObject user) {
        final JsonObject processed = this.simple.getData(user);
        return Future.succeededFuture(processed)
                .compose(item ->
                        Future.succeededFuture(item.encode()));
    }
}
```

### 1.3. Injected Java Object

```java
package up.god.micro.inject;

import io.vertx.core.json.JsonObject;

public class SimpleObject {

    public JsonObject getData(final JsonObject data) {
        data.put("className", getClass().getName());
        return data;
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.inject.SimpleActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 7 Queue ) The Zero system has found 7 components of @Queue.
[ ZERO ] Vert.x zero has found 9 incoming address from the system. Incoming address list as below: 
......
[ ZERO ]        Addr : ZERO://INJECT/SIMPLE
......
[ ZERO ] ( 1 Receipt ) The queue up.god.micro.inject.SimpleWorker scanned 1 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( 1 Inject ) The Zero system has found "up.god.micro.inject.SimpleWorker" object contains \
    1 components of @Inject or ( javax.inject.infix.* ).
......
[ ZERO ] ( Uri Register ) "/api/inject/simple" has been deployed by ZeroHttpAgent, Options = Route...
```

## 3. Testing

**URL** : [http://localhost:6083/api/inject/simple?username=Lang](http://localhost:6083/api/inject/simple?username=Lang)

**Method** : GET

**Response** :

```json
{
    "data": "{\"age\":33,\"username\":\"Lang\",\"className\":\"up.god.micro.inject.SimpleObject\"}"
}
```

## 4. Summary

The response body data is json format but string literal, because in our consumer class, the return type
is `Future<String>`, if you want to get response of standard json object, you can modify the method code to following:

```java
    @Address("ZERO://INJECT/SIMPLE")
    public Future<JsonObject> process(final JsonObject user) {
        final JsonObject processed = this.simple.getData(user);
        return Future.succeededFuture(processed);
    }
```

Then the response data should be as following:

```json
{
    "data": {
        "age": 33,
        "username": "Lang",
        "className": "up.god.micro.inject.SimpleObject"
    }
}
```

Then the response data could be parsed easily.

