# Enable Basic Authorization

You can enable authorization in zero system as following steps:

## 1. Configuration

In your configuration `vertx.yml`, you must define lime extend node as following:

```yaml
zero:
  lime: secure
```

Then it means that you must create new up.god.file named `vertx-secure.yml` instead with following content:

```yaml
secure:
  # Standard Type
  mongox:
    type: mongo
    config:
      collectionName: DB_USER
      saltStyle: NO_SALT
```

Zero system provide some standard authorization by type \( Now support **mongo** \).

## 2. Create new class

Then you can create new class as following:

```java
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.up.annotations.Authenticate;
import io.vertx.up.annotations.Wall;
import io.vertx.mod.plugin.mongo.MongoInfix;
import io.vertx.up.secure.component.BasicOstium;

@Wall(value = "mongox", path = "/exp4/*")
public class MongoKeeper {

    @Authenticate
    public AuthHandler authenticate(final JsonObject config) {
        return BasicOstium.create(
            MongoAuth.create(MongoInfix.getClient(), config)
        );
    }
}
```

This class is annotated with @Wall, if the **path** is not set, it will be the value `/*` for all routes, the value
should be configured in `vertx-secure.yml.`in current example it's `mongox`. You can define more than one walls for each
routes. Then you must create the `AuthHandler` method to create the AuthHandler, now you can use `BasicOstium` to create
basic authorization handler, also this method must be annotated with `@Authenticate`.

* The method must be annotated with `@Authenticate` to mean that this method will provide `AuthHandler` for
  authenticate.
* In this class, you must mot use any Inject Dependency to get instance such as `MongoClient`, because this @Wall will
  be processed in start up phase instead of request phase.
* We recommend to use `BasicOstium` instead of standard vert.x `BasicAuthHandler` because the class `BasicOstium` will
  bind to Resource Model instance `Envelop` to provide zero format http response.

## 3. Example response

Once you set the @Wall, you must send request with **Authorization** http header or your'll get following response:

```json
{
    "code": -60012,
    "message": "[ERR-60012] (BasicPhylum) Web Exception occus: (401) - (Security) Unauthorized request met in request."
}
```



