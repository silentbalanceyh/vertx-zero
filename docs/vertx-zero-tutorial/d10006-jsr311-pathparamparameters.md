# JSR311, @PathParam...Parameters

Path variables are common used in Restful endpoint, zero system also support path variable with
annotation `javax.ws.rs.PathParam`, you can define path parameters in your code.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public class PathParamExecutor {

    @Path("param/path1/{name}")
    @GET
    public String sayPath(
            @PathParam("name") final String name
    ) {
        return "Hello: Path Get: " + name;
    }

    @Path("param/path1/{name}")
    @POST
    public String sayPath2(
            @PathParam("name") final String name
    ) {
        return "Hello: Path Post: " + name;
    }

    @Path("param/path1/{name}")
    @PUT
    public String sayPath3(
            @PathParam("name") final String name
    ) {
        return "Hello: Path Put: " + name;
    }
}
```

_In zero system, all http methods include POST/PUT also support path variables to extract and put in using in future._

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 3 Event ) The endpoint up.god.micro.params.PathParamExecutor scanned 3 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/path1/:name" has been deployed by ZeroHttpAgent, Options = Route...
    order:5000000 methods:[POST]]@738775182.
[ ZERO ] ( Uri Register ) "/api/param/path1/:name" has been deployed by ZeroHttpAgent, Options = Route...
    order:5000000 methods:[PUT]]@286864174.
[ ZERO ] ( Uri Register ) "/api/param/path1/:name" has been deployed by ZeroHttpAgent, Options = Route...
    order:5000000 methods:[GET]]@775245096.
......
```

## 3. Testing

### 3.1. Get Request

**URL** : [http://localhost:6083/api/param/path1/lang.yu@hpe.com](http://localhost:6083/api/param/path1/lang.yu@hpe.com)

**Method** : GET

**Response**:

```json
{
    "data": "Hello: Path Get: lang.yu@hpe.com"
}
```

### 3.2. Post Request

**URL** : [http://localhost:6083/api/param/path1/lang.yu@hpe.com](http://localhost:6083/api/param/path1/lang.yu@hpe.com)

**Method** : POST

**Response**:

```json
{
    "data": "Hello: Path Post: lang.yu@hpe.com"
}
```

### 3.3. Put Request

**URL** : [http://localhost:6083/api/param/path1/lang.yu@hpe.com](http://localhost:6083/api/param/path1/lang.yu@hpe.com)

**Method** : PUT

**Response**:

```json
{
    "data": "Hello: Path Put: lang.yu@hpe.com"
}
```

## 4. Summary

You can use JSR311 annotations to setup your request path variables, but there are one rule that need to know:

_Path variables often let you set duplicated uri, such as _`/api/{name}`_ and _`/api/{age}`_, do not set previous two
uri with the same Http methods because zero system will consider these two are the same and will ignore one here._

* GET: `/api/{name}`
* GET: `/api/{age}`

Above two examples are wrong for path variables.

