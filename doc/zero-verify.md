# Advanced Validation

Once the parameter are annotated with @BodyParam and typed with JsonObject, zero system also support another method to
verify request data here.

## 1. Source Code

```java
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/zero/type")
public class BasicTypeApi {

    @Path("/json")
    @POST
    public String testInteger(
            @BodyParam
            @Codex final JsonObject data) {
        return "Number: " + data.encode();
    }
}
```

## 2. Configuration

In this kind of situation, you need to use `@Codex` annotation only, but you must provide the configuration up.god.file
in your class path as following:

```
src/main/resources/codex/zero.type.json.post.yml
```

The configuration up.god.file name should be `path + method`, once you met the path variable such as `:name`, you can
replace `:` with `_` instead to set the filename.

## 3. Yml content

```yaml
username:
- type: "required"
  message: "User name should not be null.！"
- type: "length"
  min: 6
  max: 16
  message: "User name length must be between 6 adn 16！"
password:
- type: "required"
  message: "Password should not be null！"
- type: "minlength"
  min: 8
  message: "Password min length should be 8！"
```

* Each field should be a node ( `key = JsonArray` ), the json array listed all the rules that will be applied to this
  field.
* Each element of JsonArray must contains `type` and `message` attribute to describe the rule, other attributes are
  configuration.
* For the type, please refer following chapter.

## 4. Support Validation Type

* [x] `required`: No configuration
* [x] `length`：Configuration: `min, max`
* [x] `minlength`：Configuration：`min`
* [x] `maxlength`：Configuration：`max`

Others are in future...