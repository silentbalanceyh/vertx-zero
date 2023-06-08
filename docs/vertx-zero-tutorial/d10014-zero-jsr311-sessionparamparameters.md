# Zero JSR311, @SessionParam...Parameters

In zero system, the session variables management is different from other variables because this variable is not provided
by Http Request directly. Instead the user will send some request and stored the data into session first, and then when
they send the request with the same session \( vertx-web.session is the same \), you could use the session variable in
your application/code logical.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.SessionData;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public class SessionParamExecutor {

    @POST
    @Path("param/session/{id}")
    @SessionData("user")
    public String saveSession(
            @PathParam("id") final String id) {
        System.out.println(id);
        return id;
    }

    @GET
    @Path("param/session")
    public String saySession(
            @SessionParam("user") final String user
    ) {
        System.out.println(user);
        return user;
    }
}
```

## 2. Console

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.SessionParamExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/session" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/session/:id" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

Current tutorial testing is different from previous because here we need two test cases.

### 3.1. Session Missing

**URL** : [http://localhost:6083/api/param/session](http://localhost:6083/api/param/session)

**Method**: GET

**Response** :

```json
{
    "data": null
}
```

### 3.2. Session Two Steps

In this test case, we need two steps to process the situation.

**1 - First**

**URL** : [http://localhost:6083/api/param/session/lang.yu-vertx](http://localhost:6083/api/param/session/lang.yu-vertx)

**Method**: POST

**Response** :

```json
{
    "data": "lang.yu-vertx"
}
```

**2 - Second**

Repeat the step of 3.1, you'll found different response from zero, it's different from test case of 3.1.

```json
{
    "data": "lang.yu-vertx"
}
```

## 4. Summary

This tutorial describes the session usage in zero system, with JSR311 zero system defined new annotation to process
session variable in zero system. Here are additional annotation such as:

* `io.vertx.up.annotations.SessionData`

This annotation provide session key as attribute to store session data, it will capture the method return value and
stored into session, then we could use @SessionParam annotation to pick up the value that you stored in previous
request.

