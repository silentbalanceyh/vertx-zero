# JSR311, @FormParam...Parameters

When you submit a form request with HTML, you often require the server could accept form parameters.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class FormParamExecutor {

    @Path("param/form")
    @GET
    public String sayFormGet(
            @FormParam("username") final String username,
            @FormParam("password") final String password) {
        return "Hello: GET: " + username + ", your password is: " + password;
    }

    @Path("param/form")
    @POST
    public String sayFormPost(
            @FormParam("username") final String username,
            @FormParam("password") final String password) {
        return "Hello: Post: " + username + ", your password is: " + password;
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.FormParamExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/form" has been deployed by ZeroHttpAgent, Options = Route... \
    order:5000000 methods:[POST]]@1781967506.
[ ZERO ] ( Uri Register ) "/api/param/form" has been deployed by ZeroHttpAgent, Options = Route... \
    order:5000000 methods:[GET]]@570377242.
......
```

## 3. Testing

Be sure you will send form request instead of restful request here.

### 3.1. Get Request

_You should write some static HTML form to submit Form Get Request instead of some tools because most of testing tools
do not support send Form Get Request_

### 3.2. Post Request

**URI**: [http://localhost:6083/api/param/form](http://localhost:6083/api/param/form)

**Method**: POST

Here we provide form request screen shot with Postman tool

![](/doc/image/form-request.png)You should get response as following.

**Response**:

```json
{
    "data": "Hello: Post: Lang, your password is: 11111111"
}
```

## 4. Summary

From above screen shot you'll see `form-data` and `x-www-form-urlencoded` both, for zero system it's the same because
zero system will decode the data automatically, don't wrong about form request type when you are using zero system.

