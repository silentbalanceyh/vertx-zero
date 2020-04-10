# D10008 - JSR311, @HeaderParam...Parameters

Except data part, you also could use @HeaderParam to get Http Header data from request.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;

@EndPoint
@Path("/api")
public class HeadParamExecutor {

    @POST
    @Path("/param/head")
    public JsonObject sayHead(
            @HeaderParam(HttpHeaders.CONTENT_TYPE) final String content,
            @HeaderParam(HttpHeaders.ACCEPT) final String accept
    ) {
        return new JsonObject()
                .put("content-type", content)
                .put("accept", accept);
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.params.HeadParamExecutor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/head" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

**URI**: [http://localhost:6083/api/param/head](http://localhost:6083/api/param/head)

**Method**: POST

**Headers**:

```
Content-Type: application/json
Authorization: Basic TEXT
```

**Response**:

```json
{
    "data": {
        "content-type": "application/json",
        "accept": "*/*"
    }
}
```

## 4. Summary

Here you can use JSR311 annotation @HeaderParam to extract header value by key directly and use it in future.

