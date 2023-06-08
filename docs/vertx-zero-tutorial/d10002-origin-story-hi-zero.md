# Origin Story, Hi Zero

You may meet many stories in software engineering, the first program called "Hello World", I think maybe you have been
very tired to meet this old friend that called "Hello World", so we call the first program named "Origin", it means that
all your stories could start from here. This tutorial will describe the first simple api development.

## 1. Source Code

Once your environment have been set up and you have started the first launcher, you can write your first restful
endpoint. Zero restful kernel specification is JSR 311
\( [JAX-RS: The Java API for RESTful Web Services](https://jcp.org/en/jsr/detail?id=311) \) , except JSR311 zero also
extend it to create more useful annotation to satisfy business requirement.

The demo project is in `vertx-zeus` , the project name is `up-rhea`.

* **Standalone - 8083**: `up-rhea`

```java
package up.god.micro.origin;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public class FirstHi {

    @GET
    @Path("hi")
    public String hi(@QueryParam("name") final String name) {
        return null == name ?
                "Hi, Input your name" :
                "Hi " + name + ", welcome to Origin";
    }
}
```

Then restart zero, you should see following information in your console:

```shell
......
[ ZERO ] ( 1 EndPoint ) The Zero system has found 1 components of @EndPoint.
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.origin.FirstHi scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/hi" has been deployed by ZeroHttpAgent, Options = Route[ ....
[ ZERO ] ( Http Server ) ZeroHttpAgent Http Server has been started successfully. \
    Endpoint: http://172.20.16.41:6083/.
......
```

## 2. Testing

Once zero is up, you can test this endpoint with postman tool or curl, you should get following results:

### 2.1. Request 1

**URL**: [http://localhost:6083/api/hi](http://localhost:6083/api/hi)  
**Method**: GET

```json
{
    "data": "Hi, Input your name"
}
```

### 2.2. Request 2

**URL**: [http://localhost:6083/api/hi?name=Lang](#)  
**Method**: GET

```json
{
    "data": "Hi Lang, welcome to Origin"
}
```

## 3. Summary

Now zero is up, the first example has been finished. here `@GET, @Path, @QueryParam` belong to JSR 311, you also could
use other JSR 311 annotations to write different endpoint. But one thing is that you must define the class as EndPoint
with zero annotation `io.vertx.up.annotations.EndPoint` , it will tell zero system to scan this class to extract all the
restful endpoints that will be published. In total:

* All api classes must be annotated with `io.vertx.up.annotations.EndPoint`, this kind of classes will be scanned by
  zero.
* Zero implemented some part of JSR311, you can use JSR311 annotations such as `javax.ws.rs.@GET`, `javax.ws.rs.@Path`,
  etc.
* Your methods must be marked with one of the method annotations of JSR311, if you missed these annotations, zero system
  will ignore the method and that will not be mounted to routing system.



