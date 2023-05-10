# JSR311, @GET, @POST...Http Method

This tutorial will focus on different Http Method usage based on JSR311. Here are annotations of JSR311 for Http Method.

* `javax.ws.rs.GET`
* `javax.ws.rs.POST`
* `javax.ws.rs.DELETE`
* `javax.ws.rs.PUT`
* `javax.ws.rs.HEAD`
* `javax.ws.rs.OPTIONS`
* `javax.ws.rs.PATCH`

Zero system support all method mounting on routing system, the common usage of restful api
contains `GET, DELETE, POST, PUT` methods.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Method usage

Please refer following entire class source code for different usage of Http Method

```java
package up.god.micro.method;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public class MethodUsage {

    @GET
    @Path("/method")
    public String sayGet() {
        return "Hi, HttpMethod = GET";
    }

    @POST
    @Path("/method")
    public String sayPost() {
        return "Hi, HttpMethod = POST";
    }

    @DELETE
    @Path("/method")
    public String sayDelete() {
        return "Hi, HttpMethod = DELETE";
    }

    @PUT
    @Path("/method")
    public String sayPut() {
        return "Hi, HttpMethod = PUT";
    }

    @OPTIONS
    @Path("/method")
    public String sayOptions() {
        return "Hi, HttpMethod = OPTIONS";
    }

    @PATCH
    @Path("/method")
    public String sayPatch() {
        return "Hi, HttpMethod = PATCH";
    }
}
```

Above example contains 6 http methods usage in zero system, once you have finished, you can start up zero.

## 2. Console

You should see following information in console when starting zero:

```shell
......
[ ZERO ] ( 6 Event ) The endpoint up.god.micro.method.MethodUsage scanned 6 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[PUT]]@685473710.
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[PATCH]]@1243852804.
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[OPTIONS]]@766024960.
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[POST]]@675804188.
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[DELETE]]@935640745.
[ ZERO ] ( Uri Register ) "/api/method" has been deployed by ZeroHttpAgent, Options = Route... \
    ...methods:[GET]]@1098033693.
......
```

You could see the suffix of each logging line for routing to describe the method. different method could be triggered by
different Http method in request.

## 3. Testing

Once zero is up, you can test the same uri with different Http Method.

### 3.1. GET Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: GET

**Response**:

```json
{
    "data": "Hi, HttpMethod = GET"
}
```

### 3.2. POST Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: POST

**Response**:

```json
{
    "data": "Hi, HttpMethod = POST"
}
```

### 3.3. PUT Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: PUT

**Response**:

```json
{
    "data": "Hi, HttpMethod = PUT"
}
```

### 3.4. DELETE Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: DELETE

**Response**:

```json
{
    "data": "Hi, HttpMethod = DELETE"
}
```

### 3.5. OPTIONS Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: OPTIONS

**Response**:

```json
{
    "data": "Hi, HttpMethod = OPTIONS"
}
```

### 3.6. PATCH Request

**URL**: [http://localhost:6083/api/method](http://localhost:6083/api/method)

**Method**: PATCH

**Response**:

```json
{
    "data": "Hi, HttpMethod = PATCH"
}
```

## 4. Summary

Current tutorial described different http method usage in zero system based on JSR311, JSR311 only support 7 annotations
for `GET, POST, DELETE, OPTIONS, PATCH, PUT, HEAD`. They are all supported by zero system.

* The common usage are `GET, DELETE, PUT, POST`
* In some cross domain business requirement, the `OPTIONS` method will be used, you can ignore the user
  defined `OPTIONS` instead of write the method code logical.
* We recommend to use `PATCH` method to update some parts of information instead of `PUT` method because zero now
  support `PATCH` method.



