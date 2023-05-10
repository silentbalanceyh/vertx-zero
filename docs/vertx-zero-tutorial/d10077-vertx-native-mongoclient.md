# Vert.x Native, MongoClient

For mongo db configuration you could refer previous
tutorials [D10077 - Reference, Mongo Setup ](d10077-third-part-mongo-setup.md)to check more details, now this chapter
we'll go back to zero system to see how to enable vert.x native client for mongo db.

Demo projects:

* **Standalone - 6073**: `up-gaea`

## 1. Configuration

This chapter is duplicated with [D10073 - Configuration, vertx-mongo.yml](d10073-configuration-vertx-mongoyml.md),
because that's the preparing steps for mysql client.

### 1.1. vertx.yml

```yaml
zero:
  lime: mysql, mongo
  vertx:
    instance:
    - name: vertx-zeus
      options:
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-inject.yml

```yaml
mongo: io.vertx.tp.plugin.mongo.MongoInfix
```

### 1.3. vertx-mongo.yml

All the mongo configurations are put into `vertx-mongo.yml` the root node `mongo` as following:

```yaml
mongo:
  db_name: ZERO_MESH
  port: 6017
  host: 127.0.0.1
  connection_string: mongodb://localhost:6017
  username: zero_mongo
  password: zero_mongo
```

All above configuration is for mongo native configuration supported, but for `up-gaea` project, there need some
additional configuration \( Server Config \).

## 2. Server Configuration

### vertx-server.yml

```yaml
server:
- name: up-gaea
  type: http
  config:
    port: 6073
    host: 0.0.0.0
```

Finally when you have finished configuration, the folder structure of project should be as following:

![](/doc/image/d10078-2.png)

## 3. Source Code

### 3.1. Api

```java
package up.god.micro.mongo;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public interface MongoApi {

    @Path("native/test")
    @POST
    @Address("ZERO://QUEUE/NATIVE/MONGO")
    JsonObject sayMongo(@BodyParam JsonObject params);
}
```

### 3.2.Consumer

```java
package up.god.micro.mongo;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import javax.inject.infix.Mongo;

@Queue
public class MongoWorker {

    @Mongo
    private transient MongoClient client;

    @Address("ZERO://QUEUE/NATIVE/MONGO")
    public void sayMongo(final Message<Envelop> message) {
        final JsonObject data = Ux.getJson(message);
        this.client.insert("DB_TEST", data, res -> {
            if (res.succeeded()) {
                System.out.println(res.result());
                message.reply(Ux.to(data));
            } else {
                res.cause().printStackTrace();
                message.reply(Envelop.ok());
            }
        });
    }
}
```

## 4. Console

When you started the application you should see following output:

```shell
......
Cluster created with settings {hosts=[localhost:6017], mode=SINGLE, requiredClusterType=UNKNOWN, serverSelectionTimeout='30000 ms', maxWaitQueueSize=500}
Opened connection [connectionId{localValue:1, serverValue:5}] to localhost:6017
Monitor thread successfully connected to server with description ServerDescription{address=localhost:6017, type=STANDALONE, state=CONNECTED, ok=true, version=ServerVersion{versionList=[3, 6, 2]}, minWireVersion=0, maxWireVersion=6, maxDocumentSize=16777216, logicalSessionTimeoutMinutes=30, roundTripTimeNanos=5597013}
......
```

## 5. Testing

Then you could test with postman

**URL** : [http://localhost:6073/api/native/test](http://localhost:6073/api/native/test)

**Method** : POST

**Request** :

```json
{
    "username":"Lang",
    "password":"pl,okmijnuhb123",
    "email":"lang.yu@email.com"
}
```

**Response** :

```json
{
    "data": {
        "username": "Lang",
        "password": "pl,okmijnuhb123",
        "email": "lang.yu@email.com",
        "_id": "5a8011506b0f4bc139b3f6ce"
    }
}
```

## 6. Summary

Here this record has been added into mongo database, you can check the result from mongo db:

![](/doc/image/d10078-1.png)

The record is you added in just request.





