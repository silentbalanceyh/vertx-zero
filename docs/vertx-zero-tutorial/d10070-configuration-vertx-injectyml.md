# Configuration, vertx-inject.yml

This configuration up.god.file provide extension system especially for vert.x native components. The default content is
as following:

```yaml
logger: io.horizon.log.internal.Log4JAnnal
```

It's the only one component that enabled by default, you can replace this component by yourself when you want to use ELK
or other logger. This chapter we'll focus on frequently used `injection` component so that you could know how to set the
configuration in zero system.

The data format of `vertx-inject.yml` is simple and it's `key: value` format.

## 1. Infixes

### 1.1. jooq

If you want to use Jooq extension, you can add following:

```yaml
jooq: io.vertx.mod.plugin.jooq.JooqInfix
```

### 1.2. mongo

```yaml
mongo: io.vertx.mod.plugin.mongo.MongoInfix
```

### 1.3. mysql

```yaml
mysql: io.vertx.up.plugin.jdbc.MySqlInfix
```

### 1.4. rpc

When you set zero micro system, following rpc injection is required for service communication

```yaml
rpc: io.vertx.mod.plugin.rpc.RpcInfix
```

## 4. Summary

Current zero system support above four standard infixes only, all above infixed should be enabled by yourself when you
want to use it, it's related to different business requirements, zero system does not enable these features
automatically. For some detail usage we'll introduce in further tutorials.

