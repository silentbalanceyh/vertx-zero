# Security, Jwt Authorization

This tutorial we'll introduce the usage of `Jwt` Authorization in zero system, the workflow is as following:

**Generate Token** :

![](/doc/image/D10104-1.png)

**Verify Token:**

![](/doc/image/D10104-2.png)

Here are two workflow in zero system that developers could define:

* **Generate Token**: When the user send request to login api, you can call `store` method to generate config and send
  config back.
* **Verify Token**: Before zero system verified config, you can check with your own code logical here.

> In vert.x native JWT support, you must set your own code logical to process config, but in zero system, you could
> focus on two functions to process config only, zero has split the workflow and let developers process JWT more
> smartly.

Demo projects:

* **Standalone - 6084**: `up-tethys`

For security configuration part you can
refer: [D10103 - Configuration, vertx-secure.yml](d10103-configuration-vertx-secureyml.md) for more details.

## 1. Source Code

### 1.1. Sender

```java
package up.god.micro.jwt;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api")
@EndPoint
public interface LoginActor {

    @POST
    @Path("/login")
    @Address("ZERO://QUEUE/LOGIN")
    JsonObject login(@BodyParam final JsonObject data);

    @POST
    @Path("/secure/jwt")
    @Address("ZERO://QUEUE/JWT")
    JsonObject secure(@BodyParam final JsonObject data);
}
```

### 1.2. Consumer

```java
package up.god.micro.jwt;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.secure.Security;

import jakarta.inject.Inject;

@Queue
public class LoginWorker {

    @Inject
    private transient Security security;

    @Address("ZERO://QUEUE/LOGIN")
    public Future<JsonObject> login(final Envelop envelop) {
        final JsonObject data = Ux.getJson(envelop);
        return Ux.Mongo.findOne("DB_USER", data)
                // 1.Once login successfully, you can call security api store to store config.
                .compose(item -> this.security.store(item));
    }


    @Address("ZERO://QUEUE/JWT")
    public Future<JsonObject> secure(final Envelop envelop) {
        return Future.succeededFuture(new JsonObject());
    }
}
```

Be careful about above code that you should `inject` the Security interface.

### 1.3. Wall

```java
package up.wall;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;
import io.vertx.up.secure.Security;
import io.vertx.up.secure.component.JwtOstium;
import io.vertx.up.secure.provider.authenticate.JwtAuth;

@Wall(value = "jwt", path = "/api/secure/*")
@SuppressWarnings("all")
public class JwtWall implements Security {

    @Authenticate
    public AuthHandler authenticate(final Vertx vertx,
                                    final JsonObject config) {
        return JwtOstium.create(JwtAuth.create(vertx, new JWTAuthOptions(config), this::verify));
    }

    @Override
    public Future<JsonObject> store(final JsonObject filter) {
        final JsonObject seed = new JsonObject()
            .put("username", filter.getString("username"))
            .put("id", filter.getString("_id"));
        // Build the data that you want to store into config.
        // 1. Generate Token
        final String config = Ux.Jwt.config(seed);
        // 2. Store config into mongo db
        return Ux.Mongo.findOneAndReplace("DB_USER", filter, "config", config);
    }

    @Override
    public Future<Boolean> verify(final JsonObject data) {
        final JsonObject extracted = Ux.Jwt.extract(data);
        // 1. Extract data from config: Authorization Header.
        final String config = data.getString("jwt");
        // 2. Set filters to check whether user id and config are matching in storage ( Mongo DB )
        final JsonObject filters = new JsonObject()
            .put("_id", extracted.getString("id"))
            .put("config", config);
        // 3. If matching, you can return Future<Boolean>, if it's true, JWT will continue.
        // If false, the workflow will be terminal and 401 replied.
        return Ux.Mongo.existing("DB_USER", filters);
    }
}
```

## 2. Summary

Once you have write above codes, you have set Jwt Authorization for `/api/secure/*` urls, in this way JWT has been
enabled. But there are some points:

* In `store` method, you could process your own code logical.
* In `verify` method, you must return Future&lt;Boolean&gt; to identify config checking result.

In real projects, the login method may be complex as following:

> Code came from Mobile App login.

```java
package com.tlk.micro.login;

import com.tlk.atom.User;
import com.tlk.infra.up.god.cv.ID;
import com.tlk.infra.exception.PasswordWrongException;
import com.tlk.infra.exception.UserNotFoundException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.typed.Uson;
import io.vertx.up.unity.Ux;
import io.vertx.up.fn.Fn;
import io.vertx.up.secure.Security;

import jakarta.inject.Inject;

public class LoginService implements LoginStub {
    @Inject
    private transient Security security;

    @Override
    @SuppressWarnings("all")
    public Future<JsonObject> login(final JsonObject params) {
        final String password = params.getString("password");
        final String username = params.getString("username");
        params.remove("password");
        return Ux.Mongo.findOne(User.TABLE, params)
            .compose(result -> Fn.get(() -> Ux.match(
                () -> Ux.fork(
                    () -> Ux.on(getClass()).on("[App] username = {0} met password wrong error.").info(username),
                    () -> Ux.thenError(PasswordWrongException.class, getClass(), username)),
                Ux.branch(null == result,
                    () -> Ux.on(getClass()).on("[App] username = {0} does not exist.").info(username),
                    () -> Ux.thenError(UserNotFoundException.class, getClass(), username)),
                Ux.branch(null != result && password.equals(result.getValue("password")),
                    () -> Ux.on(getClass()).on("[App] username = {0} login successfully."),
                    () -> Uson.create(result).convert(ID.DB_KEY, ID.UI_KEY).toFuture()))
            ))
            .compose(user -> security.store(user));
    }
}

```

Then you can write any kind of JWT code logical that you want in your projects.

