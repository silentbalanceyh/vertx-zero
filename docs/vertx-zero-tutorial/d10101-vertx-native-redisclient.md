# Vert.x Native, RedisClient

This tutorial will introduce how to use Redis in zero system.

## 1. Configuration

This chapter is duplicated with [D10102 - Configuration, vertx-redis.yml](d10102-configuration-vertx-redisyml.md),
because that's the preparing steps for redis client.

### 1.1. vertx.yml

Be sure the extension up.god.file name existing in `lime` node:

```yaml
zero:
  lime: redis
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-inject.yml

```yaml
redis: io.vertx.mod.plugin.redis.RedisInfix
```

### 1.3. vertx-redis.yml

```yaml
redis:
  host: 0.0.0.0
  port: 6379
```

Just like `vertx-mysql.yml` configuration, here all the `redis` node keys are native supported by vert.x, you can
provide all the native configuration keys to `redis` in zero system.

## 2. Source Code

```java
package up.god.micro.redis;

import io.vertx.redis.RedisClient;
import io.vertx.up.annotations.EndPoint;

import jakarta.inject.infix.Redis;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("")
@EndPoint
public class RedisActor {
    @Redis
    private transient RedisClient client;

    @Path("/api/redis")
    @GET
    public String sayRedis(final String name) {
        System.out.println(this.client);
        return "Redis, " + name;
    }
}
```

## 3. Summary

Here you can inject RedisClient based on above code.



