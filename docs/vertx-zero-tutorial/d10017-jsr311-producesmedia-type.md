# JSR311, @Produces...Media Type

Zero system could parse header `Content-Type` and produce error of _415 Unsupported Media Type_. And also it could
provide client media type parsing, but it's not the code _406_, but 404 instead. It means that when user
provide `Accept` header, once zero server could not provide the matching type endpoint, the server will tell user that
the resource could not be found. We'll use `javax.ws.rs.Produces` annotation to do the client media type matching.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.media;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@EndPoint
@Path("/api")
public class AcceptActor {

    @POST
    @Path("media/accept")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject sayJson(final JsonObject data) {
        return data;
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.media.AcceptActor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/media/accept" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Not Found

**URL** : [http://localhost:6083/api/media/accept](http://localhost:6083/api/media/accept)

**Method** : POST

**Headers** :

```
Accept: application/xml
Content-Type: application/json
```

**Response** : \( _Status Code = 404 _\)

```html
<html>
    <body>
        <h1>Resource not found</h1>
    </body>
</html>
```

### 3.2. Correct Request

**URL** : [http://localhost:6083/api/media/accept](http://localhost:6083/api/media/accept)

**Method** : POST

**Headers** :

```
Accept: application/json
Content-Type: application/json
```

**Response** :

```json
{
    "data": {
        "username": "request"
    }
}
```

## 4. Summary

Here leave a problem for headers, you also need to setup `Content-Type` header in your request, then you can
set `Accept` for media type parsing.



