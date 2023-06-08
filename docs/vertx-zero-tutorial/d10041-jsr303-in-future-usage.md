# JSR303, 2.x version

In current tutorial we have checked most of JSR303 annotation usage based on Hibernate Validator Framework. Here are
some new annotations in JSR303.

* [x] `javax.validation.constraints.Email`
* [x] `javax.validation.constraints.FutureOrPresent`
* [x] `javax.validation.constraints.Negative`
* [x] `javax.validation.constraints.NegativeOrZero`
* [x] `javax.validation.constraints.NotBlank`
* [x] `javax.validation.constraints.NotEmpty`
* [x] `javax.validation.constraints.PastOrPresent`
* [x] `javax.validation.constraints.Positive`
* [x] `javax.validation.constraints.PositiveOrZero`

You can set above new annotations in 2.x version and validated by Hibernate Validator.

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

import javax.validation.constraints.Email;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class EmailActor {

    @Path("email")
    @GET
    public String sayEmail(
            @Email
            @QueryParam("email") final String email
    ) {
        return "Hi, email = " + email;
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/email?email=xxxx](http://localhost:6083/api/jsr303/email?email=xxxx)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.EmailActor, method = public java.lang.String up.god.micro.validation.EmailActor.sayEmail(java.lang.String), message = must be a well-formed email address.",
    "info": "must be a well-formed email address"
}
```

If you changed the email parameter such as `lang.yu@hpe.com`, you can see following response:

```json
{
    "data": "Hi, email = lang.yu@hpe.com"
}
```

## 3. Summary

In total, zero system support most of JSR303 specification because it used Hibernate Validator to do it. Another feature
that zero system provided is that it extend JSR303 and could verify the JsonObject/JsonArray format, you can move
forward to study the next annotations.

