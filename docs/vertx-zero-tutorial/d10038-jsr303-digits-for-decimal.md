# JSR303, @Digits for decimal

This chapter focus on decimal again, except `@DecimalMin` and `@DecimalMax`, we also provide another validation for
decimal details.

* `@Digits`: this annotation contains two parts:
    * integer: it limit the integer part length;
    * fraction: it limit the decimal part length after dot;

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

import javax.validation.constraints.Digits;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class DigitActor {

    @Path("digit")
    @GET
    public String sayDigit(
            @Digits(integer = 2, fraction = 2)
            @QueryParam("digit") final Double currency
    ) {
        return "Hi, Currency is " + currency;
    }
}
```

## 2. Testing

**URL** : http://localhost:6083/api/jsr303/digit?digit=140.22

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.DigitActor, method = public java.lang.String up.god.micro.validation.DigitActor.sayDigit(java.lang.Double), message = numeric value out of bounds (<2 digits>.<2 digits> expected).",
    "info": "numeric value out of bounds (<2 digits>.<2 digits> expected)"
}
```

If you provide correct parameter you should get following correct response:

**URL** : http://localhost:6083/api/jsr303/digit?digit=40.22

```json
{
    "data": "Hi, Currency is 40.22"
}
```

## 3. Summary

This annotation is more correct to limit the decimal format include integer parts and decimal parts, then you could use
this annotation to do some validation in your code.



