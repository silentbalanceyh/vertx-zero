# Interface Annotation

The interface annotation could be as following:

## 1. Source Code

**UserApi**

```java
import io.vertx.up.annotations.EndPoint;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/zero/user")
@EndPoint
public interface UserApi {

    @Path("/login")
    @GET
    String login(
            @QueryParam("username") @NotNull(message = "用户名不能为空")
                    String username,
            @QueryParam("password") @NotNull(message = "密码不能为空")
                    String password
    );
}
```

**UserActor**

```java
public class UserActor implements UserApi {

    @Override
    public String login(final String username,
                        final String password) {
        return "Hello";
    }
}
```

## 2. Console

```
[ ZERO ] ( 1 Event ) The endpoint io.vertx.up.example.api.jsr303.UserApi scanned 1 events of Event, ...
...
[ ZERO ] ( Uri Register ) "/zero/user/login" has been deployed by ZeroHttpAgent
```

## 3. Curl Testing

```
curl http://localhost:6083/zero/user/login
{"brief":"OK","status":200,"data":"Hello"}
```

## 4. Hints

* This interface must contains only one implementation class, once it's more than 1, `DuplicatedImplException` will
  throw out.