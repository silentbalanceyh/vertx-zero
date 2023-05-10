# Zero JSR311, @BodyParam...Parameters

Because body request often used in restful web service application, zero system extend JSR311 and defined new
annotations for common usage.

* `jakarta.ws.rs.BodyParam`
* `jakarta.ws.rs.SessionParam`
* `jakarta.ws.rs.StreamParam`

Above three annotations are defined by zero system, because it's useful in different requirements, current tutorial will
describe the usage of @BodyParam. There are some simple rules for @BodyParam:

* There is no field name for @BodyParam, you must use it directly.
* The @BodyParam will bind to special java types, not all will be supported.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

This example describe send the data to zero and serialized in two vertx types:

* `io.vertx.core.json.JsonArray`
* `io.vertx.core.json.JsonObject`

Code:

```java
package up.god.micro.params;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class BodyParamExecutor {

    @POST
    @Path("/param/body/json")
    public JsonObject sayJson(
            @BodyParam final JsonObject json
    ) {
        return json;
    }

    @POST
    @Path("/param/body/jarray")
    public JsonArray sayJArray(
            @BodyParam final JsonArray jarray
    ) {
        return jarray;
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.BodyParamExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/body/jarray" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/body/json" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. JsonObject

**URI**: [http://localhost:6083/api/param/body/json](http://localhost:6083/api/param/body/json)

**Method**: POST

**Request**:

```json
{
    "username":"Lang.Yu",
    "mobile":"15922X114XX"
}
```

**Response**:

```json
{
    "data": {
        "username": "Lang.Yu",
        "mobile": "15922X114XX"
    }
}
```

### 3.2. JsonArray

**URI**: [http://localhost:6083/api/param/body/jarray](http://localhost:6083/api/param/body/jarray)

**Method**: POST

**Request**:

```json
[
    {
        "username":"Lang.Yu",
        "mobile":"15922X114XX"
    }
]
```

**Response**:

```json
{
    "data": [
        {
            "username": "Lang.Yu",
            "mobile": "15922X114XX"
        }
    ]
}
```

## 4. Summary

Above examples showed that how to read request body in zero system, if your request format is invalid, you'll get error
from zero system.

For example you provided following json format:

```json
{
    "username":"Lang.Yu",
    "mobile":"15922X114XX",
}
```

Above json format is invalid and it could not be converted into JsonObject, you'll get following error \( 400 Bad
Request \):

```json
{
    "code": -60004,
    "message": "[ERR-60004] (JsonObjectSaber) Web Exception occus: (400) - Zero system detect ..."
}
```

Do remember that you should provide correct data format to zero system or zero system will reject your request with
above standard errors.

