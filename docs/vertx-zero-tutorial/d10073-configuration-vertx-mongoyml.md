# Configuration, vertx-mongo.yml

Another native client that zero system supported is `MongoClient` in
vert.x, [Reference](http://vertx.io/docs/vertx-mongo-client/java/). You can read current tutorials to check the details
about how to configure mongo client in zero system.

## 1. Configuration

### 1.1. vertx.yml

Be sure the extension up.god.file name existing in `lime` node in the major configuration

```yaml
zero:
  lime: mongo
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-inject.yml

```yaml
mongo: io.vertx.mod.plugin.mongo.MongoInfix
```

### 1.3. vertx-mongo.yml

```yaml
mongo:
  db_name: ZERO_MESH
  port: 6017
  host: 127.0.0.1
  connection_string: mongodb://localhost:6017
```

Just like `vertx-mysql.yml` configuration, here all the `mongo` node keys are native supported by vert.x, you can
provide all the native configuration keys here for mongo db, please
refer [http://vertx.io/docs/vertx-mongo-client/java/\#\_configuring\_the\_client](http://vertx.io/docs/vertx-mongo-client/java/#_configuring_the_client)
.

## 2. Summary

Here are the mongo db standalone configuration part and in forward tutorials we'll introduce the client usage in zero
system.

