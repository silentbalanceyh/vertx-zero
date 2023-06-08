# Non-Blocking, Mode 4 Experimental extension

In zero system programming, we recommend to use uniform resource model instead of the type user defined, but for some
special business requirements, many developers also want to use their own java class type. Zero system also provide
extension to developer for this kind of situations. Because it's not standard mode, in current version of zero system,
we still consider it as experimental because it may contain long progress in future to finish and may contain unexpected
bugs.

**Rules**

* The consumer class should only accept one argument, because of that the argument came from sender class.
* The consumer class argument data type must be matching or compatible with sender class return type.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

### 1.1 Pojo Class

```java
package up.god.micro.async;

import lombok.Data;

@Data
public class JavaJson {

    private String name;
    private String email;
    private Integer age;
}
```

### 1.2 Sender

```java
package up.god.micro.async;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public class JavaDirectActor {
    // String out
    @POST
    @Path("request/java-direct")
    @Address("ZERO://ASYNC/JAVA/DIRECT")
    public String sayHello(
            @QueryParam("age") final int age) {
        return String.valueOf(age);
    }

    // Pojo out
    @POST
    @Path("request/java-pojo")
    @Address("ZERO://ASYNC/JAVA/POJO")
    public JavaJson sayPojo(
            @QueryParam("age") final int age) {
        final JavaJson json = new JavaJson();
        json.setAge(age);
        json.setName("Lang");
        json.setEmail("lang.yu@hpe.com");
        return json;
    }
}
```

### 1.3 Consumer

```java
package up.god.micro.async;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

@Queue
public class JavaDirectWorker {

    @Address("ZERO://ASYNC/JAVA/DIRECT")
    public String sayHello(final String age) {
        return "Hello: " + age;
    }

    @Address("ZERO://ASYNC/JAVA/POJO")
    public String sayPojo(final JavaJson pojo) {
        return pojo.toString();
    }
}
```

## 2. Console

```shell
......
( 2 Event ) The endpoint up.god.micro.async.JavaDirectActor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( 4 Queue ) The Zero system has found 4 components of @Queue.
[ ZERO ] Vert.x zero has found 5 incoming address from the system. Incoming address list as below:
......
[ ZERO ]        Addr : ZERO://ASYNC/JAVA/DIRECT
[ ZERO ]        Addr : ZERO://ASYNC/JAVA/POJO
[ ZERO ] ( 2 Receipt ) The queue up.god.micro.async.JavaDirectWorker scanned 2 records of Receipt, \
    will be mounted to event bus.
......
[ ZERO ] ( Uri Register ) "/api/request/java-direct" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/request/java-pojo" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Direct Request

**URL** : [http://localhost:6083/api/request/java-direct?age=22](http://localhost:6083/api/request/java-direct?age=22)

**Method** : POST

**Response** :

```json
{
    "data": "Hello: 22"
}
```

### 3.2. Pojo Request

**URL**: [http://localhost:6083/api/request/java-pojo?age=22](http://localhost:6083/api/request/java-pojo?age=22)

**Method** : POST

**Response** :

```json
{
    "data": "JavaJson(name=Lang, email=lang.yu@hpe.com, age=33)"
}
```

## 4. Summary

Here we provide this mode for some special requirements, but we still recommend to use the mode of vert.x style that
will be described in next tutorials. Current mode is experimental version, there may be some bugs for this kind of
requirements, once you met bugs you can submit to https://github.com/silentbalanceyh/vertx-zero/issues 



