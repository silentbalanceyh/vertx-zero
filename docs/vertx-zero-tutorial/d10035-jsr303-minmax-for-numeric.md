# JSR303, @Min/@Max for numeric

This chapter we'll introduce the range of integer or long value for min/max limitation, it also belong to JSR303
specifications.

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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class NumericActor {

    @Path("numeric")
    @GET
    public String sayNum(
            @Min(10)
            @Max(100)
            @QueryParam("age") final Integer age,
            @Min(1)
            @QueryParam("test") final Integer test
    ) {
        return "Hello, please check your age. " + age;
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/numeric?age=9](http://localhost:6083/api/jsr303/numeric?age=9)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NumericActor, method = public java.lang.String up.god.micro.validation.NumericActor.sayNum(java.lang.Integer,java.lang.Integer), message = must be greater than or equal to 10.",
    "info": "must be greater than or equal to 10"
}
```

**
URL** : [http://localhost:6083/api/jsr303/numeric?age=12&test=-34](http://localhost:6083/api/jsr303/numeric?age=12&test=-34)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NumericActor, method = public java.lang.String up.god.micro.validation.NumericActor.sayNum(java.lang.Integer,java.lang.Integer), message = must be greater than or equal to 1.",
    "info": "must be greater than or equal to 1"
}
```

## 3. Summary

Here we could see that the result is expected to limit number range.

