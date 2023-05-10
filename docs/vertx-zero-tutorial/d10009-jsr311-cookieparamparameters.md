# JSR311, @CookieParam...Parameters

This example is a little complex to test, here we need to see the cookie value in your client tool first.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class CookieParamExecutor {

    @Path("param/cookie")
    @GET
    public String sayCookie(
            @CookieParam("cookie-id") final String cookie
    ) {
        return "Hello, Cookie: " + cookie;
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 1 Event ) The endpoint up.god.micro.params.CookieParamExecutor scanned 1 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/cookie" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

**URI**: [http://localhost:6083/api/param/cookie](http://localhost:6083/api/param/cookie)

**Method**:** **GET

**Cookie**: cookie-id=lang.yu-zero; path=/; domain=localhost; Expires=Tue, 19 Jan 2038 03:14:07 GMT;

**Response**:

```json
{
    "data": "Hello, Cookie: lang.yu-zero"
}
```

## 4. Summary

Here you can get cookie data from http request, please be sure you have set the cookies in your request. For postman
tools, you can set here:

![](/doc/image/cookie.png)

