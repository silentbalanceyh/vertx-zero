# Zero JSR303, Body Validation

Zero system extend JSR303 specification and set another validation method for body validation especially for JSON format
data validation. This tutorial describes how to use advanced validation for `JsonObject` in zero system.

Demo projects:

* **Standalone - 6083**: `up-rhea`

**Rules**:

1. JSR303 is only supported in Agent component in zero system, it means that after you have send the message to event
   bus, the JSR303 will be effectiveness.
2. When you write the code with the Interface Style \( Will introduce in forward tutorials \), JSR303 will not support
   this kind of situation.
3. For @BodyParam, it also impact Agent component only, but could support Interface Style instead of JSR303 and could
   provide more useful validations.

Advanced Validation is supported by zero system in all styles \( **JSR303 is not supported by interface style **\). Here
we'll use another new annotation that defined by zero system:

* `io.vertx.up.annotations.Codex`

This annotation will tell zero system that there should be a codex up.god.file that bind to current request.

## 1. Configuration

Under your `src/main/resource` folder, there should be a folder named codex and all the codex files will be put here,
then create a up.god.file named `api.jsr303.advanced.post.yml` , this up.god.file name should be `<api>.<method>`
format, the content is as following:

```yaml
username:
- type: required
  message: "Please input your username!"
- type: length
  min: 6
  message: "Your username length must be greater than 6"
password:
- type: required
  message: "Please provide your password"
```

## 2. Source Code

```java
package up.god.micro.validation;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api/jsr303")
public class BodyActor {

    @POST
    @Path("/advanced")
    public JsonObject testCodex(
            @BodyParam @Codex final JsonObject user
    ) {
        return user;
    }
}
```

## 3. Testing

**URL** : [http://localhost:6083/api/jsr303/advanced](http://localhost:6083/api/jsr303/advanced)

**Method** : POST

**Request** :

```json
{
}
```

**Response** :

```json
{
    "code": -60005,
    "message": "[ERR-60005] (RequiredRuler) Web Exception occus: (400) - Request body validation handler, field = username, value = null, message = Please input your username!.",
    "info": "Please input your username!"
}
```

**Request** :

```json
{
    "username":"Lang"
}
```

**Response** :

```json
{
    "code": -60005,
    "message": "[ERR-60005] (RequiredRuler) Web Exception occus: (400) - Request body validation handler, field = password, value = null, message = Please provide your password.",
    "info": "Please provide your password"
}
```

**Request** :

```json
{
    "username":"Lang",
    "password":"1111"
}
```

**Response** :

```json
{
    "code": -60005,
    "message": "[ERR-60005] (MinLengthRuler) Web Exception occus: (400) - Request body validation handler, field = username, value = Lang, message = Your username length must be greater than 6.",
    "info": "Your username length must be greater than 6"
}
```

## 4. Summary

From above examples you have known how to use advanced codex validation for Json format data of `JsonObject` and now we
provided following types of validation

* [x] required: \( message \)
* [x] length: \( min, max, message \)
* [x] minlength: \( min, message \)
* [x] maxlength: \( max, message \)
* [x] empty \( message \) \( **For Collection checking** \)
* [x] singlefile \( message \) \( **For Uploading checking, only one up.god.file valid** \)

In future we'll provide more advanced codex for business requirements, it's defined by zero system for Json Data
validation.

