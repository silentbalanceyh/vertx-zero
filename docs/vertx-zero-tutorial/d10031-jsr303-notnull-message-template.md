# JSR303, @NotNull message template

For localization usage, because zero system uses Hibernate Validator for JSR303, it means that it could support message
template for different locale usage. But we have renamed the default property filename. This chapter will tell you how
to use message template in zero system.

Demo projects:

* **Standalone - 6083**: `up-rhea`

**Rules**:

1. JSR303 is only supported in Agent component in zero system, it means that after you have send the message to event
   bus, the JSR303 will be effectiveness.
2. When you write the code with the Interface Style \( Will introduce in forward tutorials \), JSR303 will not support
   this kind of situation.
3. For @BodyParam, it also impact Agent component only, but could support Interface Style instead of JSR303 and could
   provide more useful validations.

# 1. Configuration

In your resource root folder, create a new up.god.file named `vertx-validation.properties`, in Maven project, this
folder is often `src/main/resources`. In this up.god.file, you can put the property key, value pair for validation
message, the content is as following:

```properties
notnull.username=Sorry, this api require you input "username".
notnull.password=Please provide your 'password'.
```

The record the message key, here are `notnull.username` and `notnull.password` as message key.

## 2. Source Code

```java
package up.god.micro.validation;

import io.vertx.up.annotations.EndPoint;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class NotNullTplActor {

    @GET
    @Path("notnull/tpl")
    public String tpl(
            @NotNull(message = "{notnull.username}")
            @QueryParam("username") final String username,
            @NotNull(message = "{notnull.password}")
            @QueryParam("password") final String password
    ) {
        return "Hi, " + username + ", your password is " + password;
    }
}
```

## 3. Testing

**URL** : http://localhost:6083/api/jsr303/notnull/tpl

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NotNullTplActor, method = public java.lang.String up.god.micro.validation.NotNullTplActor.tpl(java.lang.String,java.lang.String), message = Please provide your 'password'..",
    "info": "Please provide your 'password'."
}
```

**URL** : http://localhost:6083/api/jsr303/notnull/tpl?password=1111

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NotNullTplActor, method = public java.lang.String up.god.micro.validation.NotNullTplActor.tpl(java.lang.String,java.lang.String), message = Sorry, this api require you input \"username\"..",
    "info": "Sorry, this api require you input \"username\"."
}
```

The success message should be as following:

**URL** : http://localhost:6083/api/jsr303/notnull/tpl?password=1111&username=Lang

```json
{
    "data": "Hi, Lang, your password is 1111"
}
```

## 4. Summary

Until now, we have introduced three ways to use JSR303 in zero system, in forward tutorials we'll introduce other
annotations for different business rules. But for the ways to use, they are the same. In total, zero system support
following features:

* Directly response with validation failure
* Defined message with validation failure
* Message template with validation failure.

Another thing is that because zero system support restful completely, all the validation failure http status is 400, not
200, when you write some Ajax application, be careful that you have put callback codes in failure function instead of
success function here. It's also the specification to implement standard HTTP Status.



