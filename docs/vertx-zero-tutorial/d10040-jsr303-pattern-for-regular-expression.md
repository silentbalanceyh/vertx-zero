# JSR303, @Pattern for regular expression

This chapter will introduce the last annotation of JSR303.

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

import javax.validation.constraints.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class PatternActor {

    @GET
    @Path("pattern")
    public String sayPattern(
            @Pattern(regexp = "^lang[0-9]+$")
            @QueryParam("pattern") final String pattern
    ) {
        return "Hi, Pattern = " + pattern;
    }
}
```

## 2. Testing

**URL** : http://localhost:6083/api/jsr303/pattern?pattern=lang.yu

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.PatternActor, method = public java.lang.String up.god.micro.validation.PatternActor.sayPattern(java.lang.String), message = must match \"^lang[0-9]+$\".",
    "info": "must match \"^lang[0-9]+$\""
}
```

## 3. Summary

If you provide valid value, you should get following correct response:

**URL** : http://localhost:6083/api/jsr303/pattern?pattern=lang2

```json
{
    "data": "Hi, Pattern = lang2"
}
```



