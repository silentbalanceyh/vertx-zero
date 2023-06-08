# JSR303, @DecimalMin/@DecimalMax

Another range for decimal number, you should use `@DecimalMin/@DecimalMax` instead of `@Min/@Max`, these annotations
could be used to parsing decimal, currency and float numbers.

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

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class DecimalActor {

    @Path("decimal")
    @GET
    public String sayDecimal(
            @DecimalMin("0.3")
            @QueryParam("min") final Double min,
            @DecimalMax("0.7")
            @QueryParam("max") final Double max
    ) {
        return "Hi, min = " + min + ", max = " + max;
    }
}
```

## 2. Testing

**
URL **: [http://localhost:6083/api/jsr303/decimal?min=0.1&max=0.8](http://localhost:6083/api/jsr303/decimal?min=0.1&max=0.8)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.DecimalActor, method = public java.lang.String up.god.micro.validation.DecimalActor.sayDecimal(java.lang.Double,java.lang.Double), message = must be less than or equal to 0.7.",
    "info": "must be less than or equal to 0.7"
}
```

**URL** : [http://localhost:6083/api/jsr303/decimal?min=0.1](http://localhost:6083/api/jsr303/decimal?min=0.1)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.DecimalActor, method = public java.lang.String up.god.micro.validation.DecimalActor.sayDecimal(java.lang.Double,java.lang.Double), message = must be greater than or equal to 0.3.",
    "info": "must be greater than or equal to 0.3"
}
```

## 3. Summary

For above examples, all the cases showed that the decimal validation have been triggered and you got the expected
validation result as response described. If you provide correct parameters, you'll get following response:

**URL** : http://localhost:6083/api/jsr303/decimal?min=0.5&max=0.6

```json
{
    "data": "Hi, min = 0.5, max = 0.6"
}
```



