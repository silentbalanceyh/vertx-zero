# @Path annotation

Zero support two mode for `@Path` annotation.

1. Annotated on Class and Method both;
2. Annotated on Method only;

For the first point please refer [Getting Start](zero-starter.md)

## 1. Source Code

```java
package org.exmaple;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
public class ZeroNoPathApi {

    @GET
    @Path("/up/example/non-path")
    public String getZero(
            @QueryParam("name") final String name) {
        return "No Path " + name;
    }
}
```

## 2. Console

```
...
[ ZERO ] ( 1 Event ) The endpoint org.exmaple.ZeroNoPathApi scanned 1 events of Event, ...
...
[ ZERO ] ( Uri Register ) "/up/example/non-path" has been deployed by ZeroHttpAgent, ...
...
```

## 3. Curl Testing

```
curl http://localhost:6083/up/example/non-path
{"brief":"OK","status":200,"data":"No Path null"}                                                          
curl http://localhost:6083/up/example/non-path\?name\=Lang
{"brief":"OK","status":200,"data":"No Path Lang"}
```
