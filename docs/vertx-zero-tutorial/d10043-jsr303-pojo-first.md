# JSR303, Pojo First

Because we go through the zero system advanced validation system for Json, let's see the Pojo validation first.

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

Here we modified the pojo that we used in previous tutorials

```java
package up.god.micro.async;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class JavaJson {

    @NotNull
    private String name;
    @Email
    private String email;
    @Min(1)
    private Integer age;
}
```

And then we could set the validation for this object in your code

```java
package up.god.micro.validation;


import com.google.gson.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.util.Jackson;
import up.god.micro.async.JavaJson;

import javax.validation.Valid;
import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api/jsr303")
public class PojoActor {

    @Path("pojo")
    @POST
    public JsonObject sayPojo(
            @BodyParam @Valid final JavaJson json
    ) {
        return Jackson.serializeJson(json);
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/pojo](http://localhost:6083/api/jsr303/pojo)

**Method** : POST

**Request**:

```json
{
    "email":"lang.yu@hpe.com"
}
```

**Response**:

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.PojoActor, method = public io.vertx.core.json.JsonObject up.god.micro.validation.PojoActor.sayPojo(up.god.micro.async.JavaJson), message = Not Null name.",
    "info": "must not be null"
}
```

**Request** :

```json
{
    "name":"Lang",
    "email":"lang.yu"
}
```

**Response**:

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.PojoActor, method = public io.vertx.core.json.JsonObject up.god.micro.validation.PojoActor.sayPojo(up.god.micro.async.JavaJson), message = must be a well-formed email address.",
    "info": "must be a well-formed email address"
}
```

**Request** :

```json
{
    "name":"Lang",
    "email":"lang.yu@126.com",
    "age":33
}
```

**Response** :

```json
{
    "data": {
        "name": "Lang",
        "email": "lang.yu@126.com",
        "age": 33
    }
}
```

## 3. Summary

This tutorial described the usage of `@Valid` annotation for pojo validation, except this example pojo also
support `message` attribute set and message template usage for properties files.

