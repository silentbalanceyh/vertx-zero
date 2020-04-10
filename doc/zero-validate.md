# JSR 303 Validation Framework

Supported:

* [x] Basic Type parameters supported
* [x] POJO supported
* [ ] JsonObject/JsonArray ( In future )

## 1. Source Code

### 1.1. Api Class with Annotations

```java
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.example.domain.Demo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;

@Path("/zero/user")
@EndPoint
public interface UserApi {

    @Path("/login")
    @GET
    String login(
            @QueryParam("username")
            @NotNull(message = "用户名不能为空")
                    String username,
            @QueryParam("password")
            @NotNull(message = "密码不能为空")
                    String password
    );

    @Path("/authorize")
    @POST
    Demo authorize(
            @BodyParam @Valid final Demo demo);
}
```

### 1.2. Demo class ( POJO )

```java
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Demo {

    @NotNull(message = "设置名称不可为空")
    private String name;

    @NotNull(message = "Email地址不可为空")
    private String email;
}
```

### 1.3. Implementation Class

```java
import io.vertx.up.example.domain.Demo;

public class UserActor implements UserApi {

    @Override
    public String login(
            final String username,
            final String password) {
        return "Hello";
    }

    @Override
    public Demo authorize(
            final Demo demo) {
        System.out.println(demo);
        return demo;
    }
}
```

## 2. Console

```
HV000001: Hibernate Validator 6.0.5.Final
HV000238: Temporal validation tolerance set to 0.
...
[ ZERO ] ( Uri Register ) "/zero/user/authorize" has been deployed by ZeroHttpAgent, ...
[ ZERO ] ( Uri Register ) "/zero/user/login" has been deployed by ZeroHttpAgent, ...
```

## 3. Curl Testing

```json
curl http://localhost:6083/zero/user/login\?password\=12
Response ( Pure Parameters )
{
    "brief": "Bad Request",
    "status": 400,
    "code": -60000,
    "message": "[ERR-60000] (Verifier) ZeroException occus: (400) - Request validation failure, class = class io.vertx.up.example.api.jsr303.UserActor, method = public abstract java.lang.String io.vertx.up.example.api.jsr303.UserApi.login(java.lang.String,java.lang.String), information = 用户名不能为空.",
    "info": "用户名不能为空"
}

curl -l -H "Content-Type:application/json" -X POST -d '{"name":"Lang"}' http://localhost:8083/zero/user/authorize
Response ( POJO )
{
    "brief": "Bad Request",
    "status": 400,
    "code": -60000,
    "message": "[ERR-60000] (Verifier) ZeroException occus: (400) - Request validation failure, class = class io.vertx.up.example.api.jsr303.UserActor, method = public abstract io.vertx.up.example.domain.Demo io.vertx.up.example.api.jsr303.UserApi.authorize(io.vertx.up.example.domain.Demo), information = Email地址不可为空.",
    "info": "Email地址不可为空"
}
```
