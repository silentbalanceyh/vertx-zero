# Getting Start

This article is a sample demo to describe how to use vert.x zero in your project.

## 1. Source Code

```java
package org.exmaple;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/up/example")
@EndPoint
public class ZeroExpApi {

    @GET
    @Path("/first/{name}")
    public String sayZero(
            @PathParam("name") final String name) {
        return "Hello " + name;
    }
}
```

## 2. Console

Then you can run your api with the booter, for booter detail please refer to the home page of this project. You should
see following logs in your console

```
...
[ ZERO ] ( 1 Event ) The endpoint org.exmaple.ZeroExpApi scanned 1 events of Event,  ...
...
[ ZERO ] ( Uri Register ) "/up/exmaple/first/:name" has been deployed by ZeroHttpAgent, ...
...
```

## 3. Curl Testing

```
curl http://localhost:6083/up/example/first/zero
{"brief":"OK","status":200,"data":"Hello zero"}
```

## 4. Hints

This is very simple demo endpoint api. Here are two comments for this example:

* All api class must be marked with `io.vertx.up.annotation.@EndPoint`, this kind of class could be scanned by Zero.
* Zero has implemented some part of JSR311, you can use some annotations that JSR311 provided such
  as: `javax.ws.rs.@GET`, `java.ws.rs.@Path` etc.
* Your method must be marked with one of the method annotation: as `javax.ws.rs.@GET` above, Zero will ignore the method
  that was not marked with http method annotation.


