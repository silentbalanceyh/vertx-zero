# JSR303, @AssertTrue/@AssertFalse for boolean

This chapter we will go though for boolean type, it must be `true/false` literal in your request, it could describe some
useful request such as:

1. Checkbox from UI
2. Two status from UI, one for true and another for false.

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

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class AssertActor {
    @Path("assert")
    @GET
    public String sayBoolean(
            @AssertTrue
            @QueryParam("male") final Boolean isMale,
            @AssertFalse
            @QueryParam("female") final Boolean isFemale) {
        return "Hi, Lang, the parameters is 'male' = " + isMale +
                ", 'female' = " + isFemale;
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/assert](http://localhost:6083/api/jsr303/assert)

**Method** : GET

**Response**:

```json
{
    "data": "Hi, Lang, the parameters is 'male' = null, 'female' = null"
}
```

> Because this example the query parameters 'male' and 'female' are not required, that's why here we could see two null
> values output. If we provide the parameters that could not be parsed to Boolean type, you'll get following response:

```json
{
    "code": -60004,
    "message": "[ERR-60004] (ZeroSerializer) Web Exception occus: (400) - Zero system detect conversation from \"test\" to type \"class java.lang.Boolean\", but its conflict."
}
```

**URL** : [http://localhost:6083/api/jsr303/assert?male=false](http://localhost:6083/api/jsr303/assert?male=false)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.AssertActor, method = public java.lang.String up.god.micro.validation.AssertActor.sayAssert(java.lang.Boolean,java.lang.Boolean), message = must be true.",
    "info": "must be true"
}
```

Once we provide the parameter of male to 'false', it means that the validation rule will be triggered, in this kind of
situation we could see the correct error messages.

## 3. Summary

This chapter describe the usage of `@AssertTrue` and `@AssertFalse` in your code, it belong to JSR303 specification.

