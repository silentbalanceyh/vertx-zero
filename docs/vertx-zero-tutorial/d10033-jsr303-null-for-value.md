# JSR303, @Null for value

From this chapter, zero system will introduce other annotations that belong to JSR303 for other validations, also we'll
introduce Hibernate Validator specific annotation for some business requirements.

Demo projects:

* **Standalone - 6083**: `up-rhea`

**Rules**:

1. JSR303 is only supported in Agent component in zero system, it means that after you have send the message to event
   bus, the JSR303 will be effectiveness.
2. When you write the code with the Interface Style \( Will introduce in forward tutorials \), JSR303 will not support
   this kind of situation.
3. For @BodyParam, it also impact Agent component only, but could support Interface Style instead of JSR303 and could
   provide more useful validations.

## 1. Source Code

```java
package up.god.micro.validation;

import io.vertx.up.annotations.EndPoint;

import javax.validation.constraints.Null;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class NullActor {

    @Path("null")
    @GET
    public String sayNull(
            @Null(message = "Please do not provide value for username, it's system api")
            @QueryParam("username") final String username
    ) {
        return "Hi, this api is always running by " + username;
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/null?username=Lang](http://localhost:6083/api/jsr303/null?username=Lang)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NullActor, method = public java.lang.String up.god.micro.validation.NullActor.sayNull(java.lang.String), message = Please do not provide value for username, it's system api.",
    "info": "Please do not provide value for username, it's system api"
}
```

**URL** : [http://localhost:6083/api/jsr303/null](http://localhost:6083/api/jsr303/null)

**Method** : GET

**Response** :

```json
{
    "data": "Hi, this api is always running by null"
}
```

## 3. Summary

This annotation is not used often, but we still could discuss for `@Null` usage, because zero system implemented JSR303
with Hibernate Validator directly, here we only showed how to use JSR303 annotation to do business validation instead.
The scenarios of `@Null` usage is exceed our tutorial topics.



