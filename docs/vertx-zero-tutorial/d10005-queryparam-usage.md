# @QueryParam...Parameters

This tutorial focus on query string parameter usage in zero system, here zero system support two types of query string
parameters.

* Encoded
* Plain Text

Also zero system will process encoded parameters automatically and you could pass above two format of parameters.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

Here are example for `@QueryParam` annotation in zero system:

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public class QueryParamExecutor {

    @Path("param/query")
    @GET
    public String sayQuery(
            @QueryParam("name") final String name) {
        return "Hello: Get " + name;
    }

    @Path("param/query")
    @POST
    public String sayPostQuery(
            @QueryParam("name") final String name) {
        return "Hello: Post " + name;
    }

    @Path("param/query-encode")
    @GET
    public String sayEncodeQuery(
            @QueryParam("name") final String name) {
        return "Hello: Encoded " + name;
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 3 Event ) The endpoint up.god.micro.params.QueryParamExecutor scanned 3 events of Event,\
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/query" has been deployed by ZeroHttpAgent, Options = Route[...
[ ZERO ] ( Uri Register ) "/api/param/query" has been deployed by ZeroHttpAgent, Options = Route[...
[ ZERO ] ( Uri Register ) "/api/param/query-encode" has been deployed by ZeroHttpAgent, Options = Route[...
......
```

## 3. Testing

### 3.1. Get Request

**URI: **[http://localhost:6083/api/param/query?name=Lang Yu](http://localhost:6083/api/param/query?name=Lang Yu)

**Method: **GET

**Response**:

```json
{
    "data": "Hello: Get Lang Yu"
}
```

### 3.2. Post Request

**URI: **[http://localhost:6083/api/param/query?name=Lang Yu](http://localhost:6083/api/param/query?name=Lang Yu)

**Method: **POST

**Response**:

```json
{
    "data": "Hello: Post Lang Yu"
}
```

### 3.3. Get Request \( Encoded \)

**
URI**: [http://localhost:6083/api/param/query-encode?name=Lang Yu](http://localhost:6083/api/param/query-encode?name=Lang
Yu)

**Method**: GET

**Response**:

```json
{
    "data": "Hello: Encoded Lang Yu"
}
```

## 4. Summary

Zero system support both Plain Text & Encoded parameters in query string, you could annotated the parameters with JSR311
annotations directly and then in your application you can use these parameters directly.

