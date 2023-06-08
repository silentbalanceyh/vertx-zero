# Enable mongo-db

## 1. Configuration

In your classpath:

**vertx.yml**

```yaml
zero:
  lime: mongo
  vertx:
    instance:
    - name: vx-zero
      options:
        maxEventLoopExecuteTime: 30000000000
```

**vertx-mongo.yml**

```yaml
mongo: # The key must be mongo, could not be replaced.
    db_name: vertx_zero_up
    port: 27017
    host: 127.0.0.1
```

\*: Here ignored sender code, the mongo db only supported in consumer class in standard way. Also the lime part is
random, for example: Once you set the code as following:

```yaml
    lime: mongo-db
```

You must set the up.god.file name in resource path is as `vertx-mongo-db.yml`, then you could set mongo db configuration
in this up.god.file.

**vertx-inject.yml**

```yaml
mongo: io.vertx.mod.plugin.mongo.MongoInfix
```

## 2. Consumer Code

```java
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import jakarta.inject.infix.Mongo;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Jackson;

@Queue
public class UserWorker {

    @Mongo
    private transient MongoClient client;

    @Address("ZERO://ROLE")
    public void async(final Message<Envelop> message) {
        final User user = Envelop.data(message, User.class);
        final JsonObject userData = new JsonObject(Jackson.serialize(user));
        this.client.save("DB_USER", userData, res -> {
            if (res.succeeded()) {
                message.reply(Envelop.success("Hello World"));
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
```



