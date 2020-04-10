# Enable mysql database

## 1. Configuration

In your classpath:

**vertx.yml**

```yaml
zero:
  lime: mysql
  vertx:
    instance:
    - name: vx-zero
      options:
        maxEventLoopExecuteTime: 30000000000
```

**vertx-mysql.yml**

```yaml
mysql:
  host: localhost
  port: 3306
  username: root
  password: root
  database: DB_HTL
```

**vertx-inject.yml**

```yaml
mongo: io.vertx.up.plugin.jdbc.MySqlInfix
```

## 2. Consumer Code

```java
import io.vertx.ext.sql.SQLClient;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.util.Jackson;
import org.tlk.api.User;

import javax.inject.infix.MySql;
import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
public class InjectApi {

    @MySql
    private transient SQLClient client;

    @POST
    @Path("/async/inject")
    @Address("ZERO://INJECT")
    public String sendAsync(
            @BodyParam final User user) {
        final String response = Jackson.serialize(user);
        // this.client ...
        // Use client directly
        return response;
    }
}
```



