# By Typed Parameters

Except all JSR311 related annotations \( Include extend by Zero such as @BodyParam, @StreamParam etc \), zero also
support some default typed parameters, these kind of parameters do not require any annotations in your code, but with
Java Type instead directly. Zero will scan the method and pick up the parameters by type instead of annotation. All
support types are as following:

* [x] `io.vertx.core.Vertx`
* [x] `io.vertx.core.buffer.Buffer`
* [x] `io.vertx.core.eventbus.EventBus`
* [x] `io.vertx.core.http.HttpServerRequest`
* [x] `io.vertx.core.http.HttpServerResponse`
* [x] `io.vertx.core.json.JsonArray`
* [x] `io.vertx.core.json.JsonObject`
* [x] `io.vertx.ext.auth.User`
* [x] `io.vertx.ext.web.FileUpload`
* [x] `io.vertx.ext.web.RoutingContext`
* [x] `io.vertx.ext.web.Session`

All above list types are default supported by zero.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class TypedExecutor {

    @POST
    @Path("param/typed/json")
    public JsonObject sendJson(
            final JsonObject json) {
        System.out.println(json);
        return json;
    }

    @POST
    @Path("param/typed/jarray")
    public JsonArray sendArray(
            final JsonArray json) {
        System.out.println(json);
        return json;
    }

    @POST
    @Path("param/typed/request")
    public String sendObj(
            final HttpServerRequest request,
            final HttpServerResponse response
    ) {
        System.out.println(request);
        System.out.println(response);
        return "Hello, request/response.";
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 3 Event ) The endpoint up.god.micro.params.TypedExecutor scanned 3 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/typed/jarray" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/typed/json" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/typed/request" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Json Request

**URL** : [http://localhost:6083/api/param/typed/json](http://localhost:6083/api/param/typed/json)

**Method** : POST

**Request :**

```json
{
    "username":"Lang.Yu",
    "password":"11111111"
}
```

**Response** :

```json
{
    "data": {
        "username": "Lang.Yu",
        "password": "11111111"
    }
}
```

### 3.2. Json Array Request

**URL**: [http://localhost:6083/api/param/typed/jarray](http://localhost:6083/api/param/typed/jarray)

**Method**: POST

**Request** :

```json
[{
    "username":"Lang.Yu",
    "password":"111111111"
},{
    "username":"Lang.Yu1",
    "email":"lang.yu@hpe.com"
}]
```

**Response** :

```json
{
    "data": [
        {
            "username": "Lang.Yu",
            "password": "111111111"
        },
        {
            "username": "Lang.Yu1",
            "email": "lang.yu@hpe.com"
        }
    ]
}
```

### 3.3. Request/Response

**URL**: [http://localhost:6083/api/param/typed/jarray](http://localhost:6083/api/param/typed/jarray)

**Method**: POST

**Request** :

```json
{
    "username":"request"
}
```

**Response** :

```json
{
    "data": "Hello, request/response."
}
```

Here you could see the object output in console and they are not null as following:

```shell
io.vertx.ext.web.impl.HttpServerRequestWrapper@7fce20b3
io.vertx.core.http.impl.HttpServerResponseImpl@117977a3
```

## 4. Summary

This tutorial describe the typed parameters by zero system default supported, in this situation you should know how to
use typed parameters in your project.

