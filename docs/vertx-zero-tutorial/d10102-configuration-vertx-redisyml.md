# Configuration, vertx-redis.yml

From `0.4.7`, zero system support native `RedisClient` that has been provided by vert.x, you can read current tutorial
to check the details about how to configure redis client in zero system.

## 1. Configuration

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

## 2. Summary

Here are the redis standalong configuration part and in forward tutorials we'll introduce the client usage in zero
system.

