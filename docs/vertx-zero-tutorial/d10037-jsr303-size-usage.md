# JSR303, @Size usage

This chapter we'll introduce another JSR303 annotations, it's for verifying the range by `@Size`.

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

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class SizeActor {

    @Path("size")
    @GET
    public String saySize(
            @Size(min = 1, max = 20)
            @QueryParam("size") final String size
    ) {
        return "Hi, Size = " + size;
    }
}
```

## 2. Testing

**
URL** : [http://localhost:6083/api/jsr303/size?size=silentbalanceyh@126.com](http://localhost:6083/api/jsr303/size?size=silentbalanceyh@126.com)

**Method** :  GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.SizeActor, method = public java.lang.String up.god.micro.validation.SizeActor.saySize(java.lang.String), message = size must be between 1 and 20.",
    "info": "size must be between 1 and 20"
}
```

## 3. Summary

In this chapter we have seen the usage of `@Size` annotation, then we could move forward for next chapter for other
annotations usage.



