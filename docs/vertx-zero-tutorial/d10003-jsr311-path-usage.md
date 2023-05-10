# JSR311, @Path Usage for EndPoint

This tutorial will focus on the usage of JSR311 annotation `javax.ws.rs.Path`, in zero system, it support two modes:

* Annotated on Class / Method both;
* Annotated on Method only;

Demo project:

* **Standalone - 6083**: `up-rhea`

## 1. Class / Method both

The first mode is using `javax.ws.rs.Path`on class and method both as following:

```java
package up.god.micro.path;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class PathBoth {

    @Path("/path/both")
    @GET
    public String sayBoth() {
        return "Hi, welcome to path both ( Class / Method )";
    }
}
```

This example is used in previous tutorials, it's common usage here.

## 2. Method Only

Another mode for `javax.ws.rs.Path` usage is as following:

```java
package up.god.micro.path;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@EndPoint
public class PathMethod {

    @Path("/api/path/method")
    @GET
    public String sayBoth() {
        return "Hi, welcome to path both ( Method Only )";
    }
}
```

_Please compare above two examples, the _`PathMethod`_ is not annotated with _`javax.ws.rs.PATH`_ , but it's still
correct to be let zero scanning. zero system will scan above two examples both._

## 3. Console

Then start your zero application, you should see following output:

```shell
......
[ ZERO ] ( 3 EndPoint ) The Zero system has found 3 components of @EndPoint.
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.path.PathBoth scanned 1 events of Event, \
    will be mounted to routing system.
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.path.PathMethod scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/path/both" has been deployed by ZeroHttpAgent, Options = Route ...
[ ZERO ] ( Uri Register ) "/api/path/method" has been deployed by ZeroHttpAgent, Options = Route ...
[ ZERO ] ( Http Server ) ZeroHttpAgent Http Server has been started successfully. \
    Endpoint: http://172.20.16.41:6083/.
.....
```

## 4. Testing

Once zero is up, you can test both endpoints.

### 4.1. Request 1

**URL**: [http://localhost:6083/api/path/method](http://localhost:6083/api/path/method)

**Method**: GET

**Response**:

```json
{
    "data": "Hi, welcome to path both ( Method Only )"
}
```

### 4.2. Request 2

**URL**: [http://localhost:6083/api/path/both](http://localhost:6083/api/path/both)

**Method**: GET

**Response**:

```json
{
    "data": "Hi, welcome to path both ( Class / Method )"
}
```

## 5. Summary

Now zero is up, both apis have been published. From this tutorial you should know:

* `javax.ws.rs.@Path` could be used on Class/Method both.
* `javax.ws.rs.@Path` could be used on Method only.



