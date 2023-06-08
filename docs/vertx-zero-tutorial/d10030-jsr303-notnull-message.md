# D10030, JSR303, @NotNull message

This chapter is a little same as D10029, but we could define the `info` node of JSON message instead of the default
message as following:

```
must not be null
```

It's useful in many business scenarios. For example you want to write a method to verify the username and password when
logging, here are two parameters:

* username: \( Path Parameter \)
* password: \( Query Parameter \)

And in your requirement, you want to define some validation rules such as following:

* When the username is null, the message should be `Sorry, the system require you input the 'username'`.
* When the password is null, the system should say `Hi, you have not provide your password, did you forget ?`.

For above requirement you can do as following in zero.

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

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class NotNullMessageActor {

    @Path("notnull/message")
    @GET
    public String verify(
            @NotNull(message = "Sorry, the system require you input the 'username'")
            @QueryParam("username") final String username,
            @NotNull(message = "Hi, you have not provide your password, did you forget ?")
            @QueryParam("password") final String password
    ) {
        return "Hi, " + username + ", Your password is: " + password;
    }
}
```

Here we could see:

> The message attribute 'message' also belong to JSR303, Bean Validation, you can set any response error message that
> you wanted here.

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/notnull/message](http://localhost:6083/api/jsr303/notnull/message)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NotNullMessageActor, method = public java.lang.String up.god.micro.validation.NotNullMessageActor.verify(java.lang.String,java.lang.String), message = Hi, you have not provide your password, did you forget ?.",
    "info": "Hi, you have not provide your password, did you forget ?"
}
```

**
URL** : [http://localhost:6083/api/jsr303/notnull/message?password=111111](http://localhost:6083/api/jsr303/notnull/message?password=111111)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.NotNullMessageActor, method = public java.lang.String up.god.micro.validation.NotNullMessageActor.verify(java.lang.String,java.lang.String), message = Sorry, the system require you input the 'username'.",
    "info": "Sorry, the system require you input the 'username'"
}
```

The successful message should be as following, URL:

[http://localhost:6083/api/jsr303/notnull/message?password=111111&username=Lang](http://localhost:6083/api/jsr303/notnull/message?password=111111&username=Lang)

```json
{
    "data": "Hi, Lang, Your password is: 111111"
}
```

## 3. Summary

Please be careful that JSR303 for this validation do not focus on the validation sequence, it just like above examples
that the password is validated first, then the username. Here we have defined our own message for JSR303 validation
instead of default validation messages. In this situation you can work in your real business requirements for more
extensions to do user-defined business validation information.



