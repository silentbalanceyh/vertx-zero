# Enable Rpc Client

Rpc Client is used for communicating between different micro service in zero framework. Zero system support two modes

* Stream Mode \( Static \) - Refer [10.3 - Example: Single Request](/doc/103-example-single-request.md) for Examples
* Dynamic Mode - This document will describe dynamic mode about how to use RpcClient in zero system

## 1. Configuration

For Rpc default configuration, it's not needed to describe here, but we need add standard infix configuration.

**vertx.yml**

```yaml
zero:
  lime: mongo, etcd3, rpc
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

**vertx-rpc.yml**

```yaml
rpc:
  ssl: false
  uniform:
    type: PEM
```

**vertx-inject.yml**

```yaml
mongo: io.vertx.tp.plugin.mongo.MongoInfix
rpc: io.vertx.tp.plugin.rpc.RpcInfix
```

## 2. Code for Rpc

Rpc code need at least two micro services in your environment.

### 2.1. Rpc Service Provider \( Coordinator or Terminator \)

```java
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;
// service name = ipc-coeus
public class SpeakWorker {

    @Ipc(value = "IPC://EVENT/ADDR")
    public JsonObject send(final Envelop envelop) {
        final JsonObject data = envelop.data(JsonObject.class);
        data.put("role", "Terminator");
        return data;
    }
}
```

Start up console

```
    [ Up Rpc   ] <Application Name> = "zero-istio",
    [ Up Rpc   ] Configuration Rpc Point = /zero/zero-istio/ipc/routes/ipc-coeus:192.168.40.60:6884, 
    [ Up Rpc   ] Service Name = ipc-coeus,
    [ Up Rpc   ] Ipc Channel = grpc://192.168.40.60:6884
    [ Up Rpc   ] Ipc Address = 
    [ Up Rpc √ ]     IPC://EVENT/ADDR
    [ Up Rpc   ] √ Successfully to registered IPCs, wait for community......SUCCESS √
```

### 2.2. Rpc Service Consumer \( Originator \)

```java
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.tp.plugin.rpc.RpcClient;

import javax.inject.infix.Rpc;
import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
// service name = ipc-cronus
@EndPoint
@Path("/cronus")
public class SpeakApi {

    @Rpc
    private transient RpcClient client;

    @Path("/call")
    @POST
    public void rpc(@BodyParam final JsonObject data) {
        // Build Rpc Data
        this.client.connect(
                "ipc-coeus",
                "IPC://EVENT/ADDR",
                data, handler -> {
                    System.out.println(handler.result());
                });
    }
}
```

Send request to service **icp-cronus**

```json
{
  "username":"lang.yu",
  "password":"173AFAD5992A3F73A472FC09B05B1FB7"
}
```

In this demo, you'll see following data output in service **ipc-cronus**, that here put the code in above.

```json
{"username":"lang.yu","password":"173AFAD5992A3F73A472FC09B05B1FB7","role":"Terminator"}
```



