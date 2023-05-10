# Zero JSR311, @BodyParam...Pojo

In many projects, you'll defined your own Data Object with Java Class, it means that you have your own type instead
of `JsonObject` and `JsonArray`, in this situation zero also provide json serialization for different java type,
especially for some user-defined POJO class.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Source Code

Create your pojo class: `JsonUser` with lombok tool \( We recommend to use because the code could be simple \):

```java
package up.god.micro.params;

import lombok.Data;

@Data
public class JsonUser {

    private String username;
    private String email;
    private Integer age;
}
```

Then you can create your own endpoint for `JsonUser`

```java
package up.god.micro.params;

import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@EndPoint
@Path("/api")
public class PojoExecutor {

    @POST
    @Path("param/pojo")
    public JsonUser sayUser(
            @BodyParam final JsonUser user
    ) {
        return user;
    }

    @POST
    @Path("param/pojos")
    public List<JsonUser> sayUsers(
            @BodyParam final Set<JsonUser> users
    ) {
        return new ArrayList<>(users);
    }
}
```

## 2. Console

Then you should see the logs in console as following:

```shell
......
[ ZERO ] ( 2 Event ) The endpoint up.god.micro.params.PojoExecutor scanned 2 events of Event, \
    will be mounted to routing system.
......
[ ZERO ] ( Uri Register ) "/api/param/pojo" has been deployed by ZeroHttpAgent, Options = Route...
[ ZERO ] ( Uri Register ) "/api/param/pojos" has been deployed by ZeroHttpAgent, Options = Route...
......
```

## 3. Testing

### 3.1. Object Request

**URL** : [http://localhost:6083/api/param/pojo](http://localhost:6083/api/param/pojo)

**Method**: POST

**Request**:

```json
{
    "username":"Lang",
    "age":33
}
```

**Response**:

```json
{
    "data": {
        "username": "Lang",
        "age": 33
    }
}
```

### 3.2. Collection Request

**URL**: [http://localhost:6083/api/param/pojos](http://localhost:6083/api/param/pojos)

**Method**: POST

**Request**:

```json
[
    {
        "username":"Lang",
        "age":33
    },
    {
        "username":"Xi",
        "email":"lang.xi@hpe.com"
    }
]
```

**Response**:

```json
{
    "data": [
        {
            "username": "Xi",
            "email": "lang.xi@hpe.com"
        },
        {
            "username": "Lang",
            "age": 33
        }
    ]
}
```

## 4. Summary

In zero system, it support json serialization from literal to correct type for Pojo and Pojo collection, you can put
your java class after @BodyParam annotation and zero system will convert it automatically for the parameters of endpoint
method.

